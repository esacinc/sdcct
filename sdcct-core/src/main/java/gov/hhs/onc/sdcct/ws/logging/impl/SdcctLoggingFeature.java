package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsEventUtils;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxws.support.JaxWsEndpointImpl;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctLoggingFeature extends AbstractFeature implements InitializingBean {
    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctLoggingFeature.class);

    @Autowired
    private List<ContentCodec<?, ?>> codecs;

    @Autowired
    private SdcctDocumentBuilder docBuilder;

    private Map<SdcctContentType, ContentCodec<?, ?>> contentTypeCodecs;
    private ServerLoggingHookInInterceptor restServerHookInInterceptor = new ServerLoggingHookInInterceptor(this, WsMessageType.REST),
        soapServerHookInInterceptor = new ServerLoggingHookInInterceptor(this, WsMessageType.SOAP);
    private ServerLoggingProcessInInterceptor restServerProcessInInterceptor = new ServerLoggingProcessInInterceptor(this, WsMessageType.REST),
        soapServerProcessInInterceptor = new ServerLoggingProcessInInterceptor(this, WsMessageType.SOAP);
    private ServerLoggingOutInterceptor restServerOutInterceptor = new ServerLoggingOutInterceptor(this, WsMessageType.REST),
        soapServerOutInterceptor = new ServerLoggingOutInterceptor(this, WsMessageType.SOAP);
    private ClientLoggingHookInInterceptor restClientHookInInterceptor = new ClientLoggingHookInInterceptor(this, WsMessageType.REST),
        soapClientHookInInterceptor = new ClientLoggingHookInInterceptor(this, WsMessageType.SOAP);
    private ClientLoggingProcessInInterceptor restClientProcessInInterceptor = new ClientLoggingProcessInInterceptor(this, WsMessageType.REST),
        soapClientProcessInInterceptor = new ClientLoggingProcessInInterceptor(this, WsMessageType.SOAP);
    private ClientLoggingOutInterceptor restClientOutInterceptor = new ClientLoggingOutInterceptor(this, WsMessageType.REST),
        soapClientOutInterceptor = new ClientLoggingOutInterceptor(this, WsMessageType.SOAP);

    @Override
    public void initialize(Server server, Bus bus) {
        Endpoint endpoint = server.getEndpoint();

        if (endpoint instanceof JaxWsEndpointImpl) {
            initialize(endpoint, this.soapServerHookInInterceptor, this.soapServerProcessInInterceptor, this.soapServerOutInterceptor);
        } else {
            initialize(endpoint, this.restServerHookInInterceptor, this.restServerProcessInInterceptor, this.restServerOutInterceptor);
        }
    }

    @Override
    public void initialize(Client client, Bus bus) {
        initialize(client, this.soapClientHookInInterceptor, this.soapClientProcessInInterceptor, this.soapClientOutInterceptor);
    }

    @Override
    public void initialize(InterceptorProvider interceptorProv, Bus bus) {
        if (interceptorProv instanceof ClientConfiguration) {
            initialize(interceptorProv, this.restClientHookInInterceptor, this.restClientProcessInInterceptor, this.restClientOutInterceptor);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.contentTypeCodecs = this.codecs.stream().collect(SdcctStreamUtils.toMap(ContentCodec::getType, Function.identity(), HashMap::new));
    }

    public SdcctDocumentBuilder getDocBuilder() {
        return this.docBuilder;
    }

    public Map<SdcctContentType, ContentCodec<?, ?>> getContentTypeCodecs() {
        return this.contentTypeCodecs;
    }

    public <T extends WsEvent> void processEvent(Exchange exchange, Message msg, T event, WsMessageType msgType, byte ... payloadBytes) throws Exception {
        SdcctWsEventUtils.processEvent(exchange, msg, event, msgType, this.docBuilder, this.contentTypeCodecs, payloadBytes);

        this.logEvent(event);
    }

    public void logEvent(LoggingEvent event) {
        LOGGER.info(event.buildMarker(), StringUtils.EMPTY);
    }

    @Nullable
    public static String buildTxId(Exchange exchange, Message msg, RestEvent event, String propName) {
        if (msg.containsKey(propName)) {
            return SdcctWsPropertyUtils.getProperty(msg, propName);
        }

        String txId = null;

        if (exchange.containsKey(propName)) {
            txId = SdcctWsPropertyUtils.getProperty(exchange, propName);
        } else if (event.getEventType() == RestEventType.REQUEST) {
            exchange.put(propName, (txId = MDC.get(propName)));
        }

        if (txId != null) {
            msg.put(propName, txId);

            event.setTxId(txId);
        }

        return txId;
    }

    private static void initialize(InterceptorProvider interceptorProv, AbstractLoggingHookInInterceptor<?> hookInInterceptor,
        AbstractLoggingProcessInInterceptor<?> processInInterceptor, AbstractLoggingOutInterceptor<?, ?> outInterceptor) {
        List<Interceptor<? extends Message>> inInterceptors = interceptorProv.getInInterceptors();
        inInterceptors.add(hookInInterceptor);
        inInterceptors.add(processInInterceptor);

        interceptorProv.getInFaultInterceptors().add(processInInterceptor);

        interceptorProv.getOutInterceptors().add(outInterceptor);
        interceptorProv.getOutFaultInterceptors().add(outInterceptor);
    }
}
