package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.fhir.impl.FhirContentProvider;
import gov.hhs.onc.sdcct.io.impl.ByteArraySource;
import gov.hhs.onc.sdcct.json.impl.JsonCodec;
import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.net.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.net.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.ContentEncodeOptions;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.logging.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.jaxws.support.JaxWsEndpointImpl;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.InterfaceInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.transport.http.Headers;
import org.apache.cxf.ws.policy.PolicyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SdcctLoggingFeature extends AbstractFeature {
    private abstract class AbstractLoggingInterceptor<T extends WsEvent> extends AbstractPhaseInterceptor<Message> {
        protected Class<T> eventClass;
        protected Supplier<T> eventCreator;
        protected WsDirection direction;
        protected RestEndpointType endpointType;
        protected String txIdPropName;
        protected WsMessageType msgType;

        protected AbstractLoggingInterceptor(String phase, Class<T> eventClass, Supplier<T> eventCreator, WsDirection direction, RestEndpointType endpointType,
            String txIdPropName, WsMessageType msgType) {
            super(phase);

            this.eventClass = eventClass;
            this.eventCreator = eventCreator;
            this.direction = direction;
            this.endpointType = endpointType;
            this.txIdPropName = txIdPropName;
            this.msgType = msgType;
        }

        @Override
        public void handleMessage(Message msg) throws Fault {
            Exchange exchange = msg.getExchange();
            T event;
            boolean initializeEvent = false;

            if (!SdcctWsPropertyUtils.containsKey(msg, this.eventClass)) {
                msg.put(this.eventClass, this.eventCreator.get());

                initializeEvent = true;
            }

            String txId = buildTxId(exchange, msg, (event = msg.get(this.eventClass)), this.txIdPropName);

            this.buildEvent(exchange, msg, event, initializeEvent);

            try {
                this.handleMessageInternal(exchange, msg, event);
            } catch (Fault e) {
                throw e;
            } catch (Exception e) {
                throw new Fault(new Exception(String.format("Unable to handle CXF message event (txId=%s) logging.", txId), e));
            }
        }

        protected abstract void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception;

        protected T buildEvent(Exchange exchange, Message msg, T event, boolean initialize) {
            if (initialize) {
                event.setDirection(this.direction);
                event.setEndpointType(this.endpointType);
                event.setMessageType(this.msgType);
            }

            return event;
        }
    }

    private abstract class AbstractLoggingInInterceptor<T extends WsEvent> extends AbstractLoggingInterceptor<T> {
        protected AbstractLoggingInInterceptor(String phase, Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType, String txIdPropName,
            WsMessageType msgType) {
            super(phase, eventClass, eventCreator, WsDirection.INBOUND, endpointType, txIdPropName, msgType);
        }
    }

    private abstract class AbstractLoggingHookInInterceptor<T extends WsEvent> extends AbstractLoggingInInterceptor<T> {
        protected AbstractLoggingHookInInterceptor(Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType, String txIdPropName,
            WsMessageType msgType) {
            super(Phase.RECEIVE, eventClass, eventCreator, endpointType, txIdPropName, msgType);

            this.addBefore(PolicyConstants.POLICY_IN_INTERCEPTOR_ID);
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
            InputStream inStream = msg.getContent(InputStream.class), targetInStream;
            boolean delegatingInStreamAvailable = (inStream instanceof DelegatingInputStream);
            DelegatingInputStream delegatingInStream = (delegatingInStreamAvailable ? ((DelegatingInputStream) inStream) : null);
            CachedOutputStream cachedStream = new CachedOutputStream();

            IOUtils.copyAtLeast((targetInStream = (delegatingInStreamAvailable ? delegatingInStream.getInputStream() : inStream)), cachedStream,
                Integer.MAX_VALUE);

            cachedStream.flush();

            targetInStream = new SequenceInputStream(cachedStream.getInputStream(), targetInStream);

            if (delegatingInStreamAvailable) {
                delegatingInStream.setInputStream(targetInStream);
            } else {
                msg.setContent(InputStream.class, targetInStream);
            }

            msg.setContent(CachedOutputStream.class, cachedStream);
        }
    }

    private class ServerLoggingHookInInterceptor extends AbstractLoggingHookInInterceptor<WsRequestEvent> {
        public ServerLoggingHookInInterceptor(WsMessageType msgType) {
            super(WsRequestEvent.class, WsRequestEventImpl::new, RestEndpointType.SERVER, SdcctPropertyNames.WS_SERVER_TX_ID, msgType);
        }
    }

    private class ClientLoggingHookInInterceptor extends AbstractLoggingHookInInterceptor<WsResponseEvent> {
        public ClientLoggingHookInInterceptor(WsMessageType msgType) {
            super(WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
        }
    }

    private abstract class AbstractLoggingProcessInInterceptor<T extends WsEvent> extends AbstractLoggingInInterceptor<T> {
        protected AbstractLoggingProcessInInterceptor(Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType, String txIdPropName,
            WsMessageType msgType) {
            super(Phase.PRE_INVOKE, eventClass, eventCreator, endpointType, txIdPropName, msgType);
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
            CachedOutputStream cachedStream = msg.getContent(CachedOutputStream.class);
            cachedStream.close();

            SdcctLoggingFeature.this.processEvent(exchange, msg, event, this.msgType, cachedStream.getBytes());
        }
    }

    private class ServerLoggingProcessInInterceptor extends AbstractLoggingProcessInInterceptor<WsRequestEvent> {
        public ServerLoggingProcessInInterceptor(WsMessageType msgType) {
            super(WsRequestEvent.class, WsRequestEventImpl::new, RestEndpointType.SERVER, SdcctPropertyNames.WS_SERVER_TX_ID, msgType);
        }
    }

    private class ClientLoggingProcessInInterceptor extends AbstractLoggingProcessInInterceptor<WsResponseEvent> {
        public ClientLoggingProcessInInterceptor(WsMessageType msgType) {
            super(WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, WsResponseEvent event) throws Exception {
            HttpResponseEvent httpEvent = new HttpResponseEventImpl();

            buildTxId(exchange, msg, httpEvent, SdcctPropertyNames.HTTP_CLIENT_TX_ID);

            httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
            httpEvent.setEndpointType(RestEndpointType.CLIENT);
            httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
            httpEvent.setStatusCode(SdcctWsPropertyUtils.getProperty(msg, Message.RESPONSE_CODE, Integer.class));

            SdcctLoggingFeature.this.logEvent(httpEvent);

            super.handleMessageInternal(exchange, msg, event);
        }
    }

    private abstract class AbstractLoggingOutCallback<T extends WsEvent> implements CachedOutputStreamCallback {
        protected Exchange exchange;
        protected Message msg;
        protected T event;
        protected WsMessageType msgType;

        protected AbstractLoggingOutCallback(Exchange exchange, Message msg, T event, WsMessageType msgType) {
            this.exchange = exchange;
            this.msg = msg;
            this.event = event;
            this.msgType = msgType;
        }

        @Override
        public void onClose(CachedOutputStream cachedStream) {
            try {
                this.onCloseInternal(((CacheAndWriteOutputStream) cachedStream));
            } catch (Fault e) {
                throw e;
            } catch (Exception e) {
                throw new Fault(e);
            }
        }

        @Override
        public void onFlush(CachedOutputStream cachedStream) {
        }

        protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
            try {
                SdcctLoggingFeature.this.processEvent(this.exchange, this.msg, this.event, this.msgType, cachedStream.getBytes());
            } finally {
                OutputStream outStream = cachedStream.getFlowThroughStream();

                cachedStream.lockOutputStream();
                cachedStream.resetOut(null, false);

                this.msg.setContent(OutputStream.class, outStream);
            }
        }
    }

    private class ServerLoggingOutCallback extends AbstractLoggingOutCallback<WsResponseEvent> {
        public ServerLoggingOutCallback(Exchange exchange, Message msg, WsResponseEvent event, WsMessageType msgType) {
            super(exchange, msg, event, msgType);
        }
    }

    private class ClientLoggingOutCallback extends AbstractLoggingOutCallback<WsRequestEvent> {
        public ClientLoggingOutCallback(Exchange exchange, Message msg, WsRequestEvent event, WsMessageType msgType) {
            super(exchange, msg, event, msgType);
        }

        @Override
        protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
            super.onCloseInternal(cachedStream);

            HttpRequestEvent httpEvent = new HttpRequestEventImpl();

            buildTxId(exchange, msg, httpEvent, SdcctPropertyNames.HTTP_CLIENT_TX_ID);

            httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
            httpEvent.setEndpointType(RestEndpointType.CLIENT);
            httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
            httpEvent.setMethod(SdcctWsPropertyUtils.getProperty(msg, Message.HTTP_REQUEST_METHOD));
            httpEvent.setPathInfo(SdcctWsPropertyUtils.getProperty(msg, Message.PATH_INFO));
            httpEvent.setQueryString(SdcctWsPropertyUtils.getProperty(msg, Message.QUERY_STRING));
            httpEvent.setScheme(SdcctWsPropertyUtils.getProperty(msg, WsPropertyNames.HTTP_SCHEME));
            httpEvent.setUri(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URI));
            httpEvent.setUrl(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URL));

            SdcctLoggingFeature.this.logEvent(httpEvent);
        }
    }

    private abstract class AbstractLoggingOutInterceptor<T extends WsEvent, U extends AbstractLoggingOutCallback<T>> extends AbstractLoggingInterceptor<T> {
        protected AbstractLoggingOutInterceptor(Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType, String txIdPropName,
            WsMessageType msgType) {
            super(Phase.PRE_STREAM, eventClass, eventCreator, WsDirection.OUTBOUND, endpointType, txIdPropName, msgType);

            this.addBefore(StaxOutInterceptor.class.getName());
        }

        @Override
        protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
            CacheAndWriteOutputStream cachedStream = new CacheAndWriteOutputStream(msg.getContent(OutputStream.class));
            cachedStream.registerCallback(this.buildCallback(exchange, msg, event, msgType));

            msg.setContent(OutputStream.class, cachedStream);
        }

        protected abstract U buildCallback(Exchange exchange, Message msg, T event, WsMessageType msgType);
    }

    private class ServerLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsResponseEvent, ServerLoggingOutCallback> {
        public ServerLoggingOutInterceptor(WsMessageType msgType) {
            super(WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.SERVER, SdcctPropertyNames.WS_SERVER_TX_ID, msgType);
        }

        @Override
        protected ServerLoggingOutCallback buildCallback(Exchange exchange, Message msg, WsResponseEvent event, WsMessageType msgType) {
            return new ServerLoggingOutCallback(exchange, msg, event, msgType);
        }
    }

    private class ClientLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsRequestEvent, ClientLoggingOutCallback> {
        public ClientLoggingOutInterceptor(WsMessageType msgType) {
            super(WsRequestEvent.class, WsRequestEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
        }

        @Override
        protected ClientLoggingOutCallback buildCallback(Exchange exchange, Message msg, WsRequestEvent event, WsMessageType msgType) {
            return new ClientLoggingOutCallback(exchange, msg, event, msgType);
        }
    }

    private final static Map<String, Object> PRETTY_PAYLOAD_ENC_OPTS = Collections.singletonMap(ContentEncodeOptions.PRETTY_NAME, Boolean.TRUE);

    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctLoggingFeature.class);

    @Autowired
    private FhirContentProvider<?> fhirContentProv;

    private ServerLoggingHookInInterceptor restServerHookInInterceptor = new ServerLoggingHookInInterceptor(WsMessageType.REST),
        soapServerHookInInterceptor = new ServerLoggingHookInInterceptor(WsMessageType.SOAP);
    private ServerLoggingProcessInInterceptor restServerProcessInInterceptor = new ServerLoggingProcessInInterceptor(WsMessageType.REST),
        soapServerProcessInInterceptor = new ServerLoggingProcessInInterceptor(WsMessageType.SOAP);
    private ServerLoggingOutInterceptor restServerOutInterceptor = new ServerLoggingOutInterceptor(WsMessageType.REST),
        soapServerOutInterceptor = new ServerLoggingOutInterceptor(WsMessageType.SOAP);
    private ClientLoggingHookInInterceptor restClientHookInInterceptor = new ClientLoggingHookInInterceptor(WsMessageType.REST),
        soapClientHookInInterceptor = new ClientLoggingHookInInterceptor(WsMessageType.SOAP);
    private ClientLoggingProcessInInterceptor restClientProcessInInterceptor = new ClientLoggingProcessInInterceptor(WsMessageType.REST),
        soapClientProcessInInterceptor = new ClientLoggingProcessInInterceptor(WsMessageType.SOAP);
    private ClientLoggingOutInterceptor restClientOutInterceptor = new ClientLoggingOutInterceptor(WsMessageType.REST),
        soapClientOutInterceptor = new ClientLoggingOutInterceptor(WsMessageType.SOAP);

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

    private <T extends WsEvent> void processEvent(Exchange exchange, Message msg, T event, WsMessageType msgType, byte ... payloadBytes) throws Exception {
        boolean restMsgType = (msgType == WsMessageType.REST), soapMsgType = (msgType == WsMessageType.SOAP);
        Binding binding = exchange.getBinding();

        if (binding != null) {
            BindingInfo bindingInfo = binding.getBindingInfo();

            if (bindingInfo != null) {
                event.setBindingName(bindingInfo.getName().toString());
            }
        }

        Service service = exchange.getService();

        if (service != null) {
            event.setServiceName(service.getName().toString());
        }

        Endpoint endpoint = exchange.getEndpoint();

        if (endpoint != null) {
            EndpointInfo endpointInfo = endpoint.getEndpointInfo();

            if (endpointInfo != null) {
                event.setEndpointAddress(endpointInfo.getAddress());

                String endpointName = endpointInfo.getName().toString();
                event.setEndpointName(endpointName);

                if (soapMsgType) {
                    event.setPortName(endpointName);
                }
            }
        }

        BindingOperationInfo bindingOpInfo = exchange.getBindingOperationInfo();

        if (bindingOpInfo != null) {
            OperationInfo opInfo = bindingOpInfo.getOperationInfo();

            if (opInfo != null) {
                event.setOperationName(opInfo.getName().toString());

                if (soapMsgType) {
                    InterfaceInfo interfaceInfo = opInfo.getInterface();

                    if (interfaceInfo != null) {
                        event.setPortTypeName(interfaceInfo.getName().toString());
                    }
                }
            }
        }

        String encName = SdcctWsPropertyUtils.getProperty(msg, Message.ENCODING);
        Charset enc = ((encName != null) ? Charset.forName(encName) : StandardCharsets.UTF_8);

        event.setPayload(new String(payloadBytes, enc));

        if (payloadBytes.length > 0) {
            ContentCodec codec = null;

            if (restMsgType) {
                OperationResourceInfo opResourceInfo = exchange.get(OperationResourceInfo.class);

                if (opResourceInfo != null) {
                    ClassResourceInfo classResourceInfo = opResourceInfo.getClassResourceInfo();

                    if (classResourceInfo != null) {
                        event.setResourceName(JAXRSUtils.getClassQName(classResourceInfo.getResourceClass()).toString());
                    }
                }

                if ((codec = msg.get(ContentCodec.class)) == null) {
                    codec = SdcctLoggingFeature.this.fhirContentProv.findCodec(MediaType.valueOf(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE)));
                }
            } else if (soapMsgType) {
                codec = SdcctLoggingFeature.this.fhirContentProv.getFormatCodecs().get(FhirFormatType.XML);
            }

            if (codec != null) {
                try {
                    if (codec instanceof JsonCodec) {
                        this.processJsonPayload(exchange, msg, event, msgType, enc, ((JsonCodec) codec), payloadBytes);
                    } else if (codec instanceof XmlCodec) {
                        this.processXmlPayload(exchange, msg, event, msgType, enc, ((XmlCodec) codec), payloadBytes);
                    }
                } catch (Exception e) {
                    LOGGER.error(
                        String.format("Unable to process CXF message logging event (txId=%s) payload (class=%s).", event.getTxId(), codec.getClass().getName()),
                        e);
                }
            }
        }

        this.logEvent(event);
    }

    private <T extends WsEvent> void processXmlPayload(Exchange exchange, Message msg, T event, WsMessageType msgType, Charset enc, XmlCodec codec,
        byte ... payloadBytes) throws Exception {
        Document doc = ((Document) codec.decode(new ByteArraySource(payloadBytes), new DOMResult()).getNode());

        event.setPrettyPayload(new String(codec.encode(new DOMSource(doc), PRETTY_PAYLOAD_ENC_OPTS), enc));

        if (msgType == WsMessageType.SOAP) {
            Element docElem = doc.getDocumentElement();

            // TODO: process SOAP headers + faults
        }
    }

    protected <T extends WsEvent> void processJsonPayload(Exchange exchange, Message msg, T event, WsMessageType msgType, Charset enc, JsonCodec codec,
        byte ... payloadBytes) throws Exception {
        event.setPrettyPayload(new String(codec.encode(codec.decode(payloadBytes), PRETTY_PAYLOAD_ENC_OPTS), enc));
    }

    private void logEvent(LoggingEvent event) {
        LOGGER.info(event.buildMarker(), StringUtils.EMPTY);
    }

    @Nullable
    private static String buildTxId(Exchange exchange, Message msg, RestEvent event, String propName) {
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
