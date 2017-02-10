package gov.hhs.onc.sdcct.ws.logging.impl;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.json.impl.JsonCodec;
import gov.hhs.onc.sdcct.json.impl.JsonEncodeOptionsImpl;
import gov.hhs.onc.sdcct.logging.LoggingEvent;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.net.mime.utils.SdcctMediaTypeUtils;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.utils.SdcctDomUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.jaxws.support.JaxWsEndpointImpl;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.InterfaceInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

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

    public <T extends WsEvent> void processEvent(Exchange exchange, Message msg, T event, WsMessageType msgType, byte ... payloadBytes) throws Exception {
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
            ContentCodec<?, ?> codec = null;

            if (restMsgType) {
                OperationResourceInfo opResourceInfo = exchange.get(OperationResourceInfo.class);

                if (opResourceInfo != null) {
                    ClassResourceInfo classResourceInfo = opResourceInfo.getClassResourceInfo();

                    if (classResourceInfo != null) {
                        event.setResourceName(JAXRSUtils.getClassQName(classResourceInfo.getResourceClass()).toString());
                    }
                }

                codec = msg.get(ContentCodec.class);
            }

            if (codec == null) {
                if (restMsgType) {
                    FhirFormatType fhirFormatType = SdcctMediaTypeUtils.findCompatible(FhirFormatType.class,
                        MediaType.valueOf(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE)));

                    if (fhirFormatType != null) {
                        codec = this.contentTypeCodecs.get(fhirFormatType.getContentType());
                    }
                } else {
                    codec = this.contentTypeCodecs.get(SdcctMediaTypeUtils.findCompatible(SdcctContentType.class,
                        MediaType.valueOf(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE))));
                }
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
        ByteArraySource src = new ByteArraySource(payloadBytes);

        event.setPrettyPayload(new String(codec.encode(src, codec.getDefaultEncodeOptions().clone().setOption(ContentCodecOptions.PRETTY, true)), enc));

        if (msgType == WsMessageType.SOAP) {
            XdmDocument doc = this.docBuilder.build(src);
            Element docElem = doc.getDocument().getDocumentElement();

            processSoapHeaders(event, docElem);
            processSoapFault(event, docElem);
        }
    }

    private static <T extends WsEvent> void processSoapFault(T event, Element docElem) {
        Element soapFaultElem =
            DOMUtils.getFirstChildWithName(DOMUtils.getFirstChildWithName(docElem, SOAP12Constants.QNAME_SOAP_BODY), SOAP12Constants.QNAME_SOAP_FAULT);

        if (soapFaultElem == null) {
            return;
        }

        Map<String, Object> soapFaultContentMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Optional.ofNullable(DOMUtils.getFirstChildWithName(soapFaultElem, SOAP12Constants.QNAME_FAULT_CODE))
            .ifPresent(soapFaultCodeElem -> soapFaultContentMap.put(SOAP12Constants.QNAME_FAULT_CODE.getLocalPart(),
                DOMUtils.getContent(DOMUtils.getFirstChildWithName(soapFaultCodeElem, SOAP12Constants.QNAME_FAULT_VALUE))));

        List<Element> soapFaultSubcodeElems = SdcctDomUtils.findDescendantElements(soapFaultElem, SOAP12Constants.QNAME_FAULT_SUBCODE);

        if (!soapFaultSubcodeElems.isEmpty()) {
            soapFaultContentMap.put(SOAP12Constants.QNAME_FAULT_SUBCODE.getLocalPart(),
                soapFaultSubcodeElems.stream()
                    .map(soapFaultSubcodeElem -> DOMUtils.getContent(DOMUtils.getFirstChildWithName(soapFaultSubcodeElem, SOAP12Constants.QNAME_FAULT_VALUE)))
                    .collect(Collectors.toList()));
        }

        Optional.ofNullable(DOMUtils.getFirstChildWithName(soapFaultElem, SOAP12Constants.QNAME_FAULT_REASON))
            .ifPresent(soapFaultReasonElem -> soapFaultContentMap.put(SOAP12Constants.QNAME_FAULT_REASON.getLocalPart(),
                DOMUtils.getContent(DOMUtils.getFirstChildWithName(soapFaultReasonElem, SOAP12Constants.QNAME_FAULT_REASON_TEXT))));

        Optional.ofNullable(DOMUtils.getFirstChildWithName(soapFaultElem, SOAP12Constants.QNAME_FAULT_DETAIL)).ifPresent(soapFaultDetailElem -> {
            List<Element> soapFaultDetailChildElems = DomUtils.getChildElements(soapFaultDetailElem);

            if (!soapFaultDetailChildElems.isEmpty()) {
                soapFaultContentMap.put(SOAP12Constants.QNAME_FAULT_DETAIL.getLocalPart(),
                    SdcctDomUtils.mapTreeContent(() -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER), soapFaultDetailChildElems.stream()));
            }
        });

        event.setSoapFault(soapFaultContentMap);
    }

    private static <T extends WsEvent> void processSoapHeaders(T event, Element docElem) {
        List<Element> soapHeaderElems = SdcctDomUtils.findDescendantElements(docElem, SOAP12Constants.QNAME_SOAP_HEADER).stream()
            .flatMap(soapHeaderContainerElem -> DomUtils.getChildElements(soapHeaderContainerElem).stream()).collect(Collectors.toList());

        if (!soapHeaderElems.isEmpty()) {
            event.setSoapHeaders(SdcctDomUtils.mapTreeContent(() -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER), soapHeaderElems.stream()));
        }
    }

    private <T extends WsEvent> void processJsonPayload(Exchange exchange, Message msg, T event, WsMessageType msgType, Charset enc, JsonCodec codec,
        byte ... payloadBytes) throws Exception {
        event.setPrettyPayload(
            new String(codec.encode(codec.decode(payloadBytes, null), new JsonEncodeOptionsImpl().setOption(ContentCodecOptions.PRETTY, true)), enc));
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
