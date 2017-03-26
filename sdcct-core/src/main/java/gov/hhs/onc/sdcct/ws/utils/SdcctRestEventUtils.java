package gov.hhs.onc.sdcct.ws.utils;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.fhir.FhirFormatType;
import gov.hhs.onc.sdcct.json.impl.JsonCodec;
import gov.hhs.onc.sdcct.json.impl.JsonEncodeOptionsImpl;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.mime.utils.SdcctMediaTypeUtils;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.ContentCodecOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.logging.impl.SdcctLoggingFeature;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.utils.SdcctDomUtils;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.cxf.binding.Binding;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.jaxrs.model.ClassResourceInfo;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.service.Service;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.InterfaceInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.transport.http.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public final class SdcctRestEventUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(SdcctRestEventUtils.class);

    private SdcctRestEventUtils() {
    }

    public static <T extends WsEvent> void processEvent(Exchange exchange, Message msg, T event, WsMessageType msgType, SdcctDocumentBuilder docBuilder,
        Map<SdcctContentType, ContentCodec<?, ?>> contentTypeCodecs, byte ... payloadBytes) throws Exception {
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
                        codec = contentTypeCodecs.get(fhirFormatType.getContentType());
                    }
                } else {
                    codec = contentTypeCodecs.get(SdcctMediaTypeUtils.findCompatible(SdcctContentType.class,
                        MediaType.valueOf(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE))));
                }
            }

            if (codec != null) {
                try {
                    if (codec instanceof JsonCodec) {
                        processJsonPayload(exchange, msg, event, msgType, enc, ((JsonCodec) codec), payloadBytes);
                    } else if (codec instanceof XmlCodec) {
                        processXmlPayload(exchange, msg, event, msgType, enc, ((XmlCodec) codec), docBuilder, payloadBytes);
                    }
                } catch (Exception e) {
                    LOGGER.error(
                        String.format("Unable to process CXF message logging event (txId=%s) payload (class=%s).", event.getTxId(), codec.getClass().getName()),
                        e);
                }
            }
        }
    }

    private static <T extends WsEvent> void processXmlPayload(Exchange exchange, Message msg, T event, WsMessageType msgType, Charset enc, XmlCodec codec,
        SdcctDocumentBuilder docBuilder, byte ... payloadBytes) throws Exception {
        ByteArraySource src = new ByteArraySource(payloadBytes);

        event.setPrettyPayload(new String(codec.encode(src, codec.getDefaultEncodeOptions().clone().setOption(ContentCodecOptions.PRETTY, true)), enc));

        if (msgType == WsMessageType.SOAP) {
            XdmDocument doc = docBuilder.build(src);
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

    private static <T extends WsEvent> void processJsonPayload(Exchange exchange, Message msg, T event, WsMessageType msgType, Charset enc, JsonCodec codec,
        byte ... payloadBytes) throws Exception {
        event.setPrettyPayload(
            new String(codec.encode(codec.decode(payloadBytes, null), new JsonEncodeOptionsImpl().setOption(ContentCodecOptions.PRETTY, true)), enc));
    }

    public static HttpRequestEvent createHttpRequestEvent(Message msg, String txIdPropName, RestEndpointType endpointType) {
        HttpRequestEvent httpEvent = new HttpRequestEventImpl();

        SdcctLoggingFeature.buildTxId(msg.getExchange(), msg, httpEvent, txIdPropName);

        httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
        httpEvent.setEndpointType(endpointType);
        httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
        httpEvent.setMethod(SdcctWsPropertyUtils.getProperty(msg, Message.HTTP_REQUEST_METHOD));
        httpEvent.setPathInfo(SdcctWsPropertyUtils.getProperty(msg, Message.PATH_INFO));
        httpEvent.setQueryString(SdcctWsPropertyUtils.getProperty(msg, Message.QUERY_STRING));
        httpEvent.setScheme(SdcctWsPropertyUtils.getProperty(msg, WsPropertyNames.HTTP_SCHEME));
        httpEvent.setUri(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URI));
        httpEvent.setUrl(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URL));

        return httpEvent;
    }

    public static HttpResponseEvent createHttpResponseEvent(Message msg, String txIdPropName, RestEndpointType endpointType) {
        HttpResponseEvent httpEvent = new HttpResponseEventImpl();

        SdcctLoggingFeature.buildTxId(msg.getExchange(), msg, httpEvent, txIdPropName);

        httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
        httpEvent.setEndpointType(endpointType);
        httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));

        Integer statusCode = SdcctWsPropertyUtils.getProperty(msg, Message.RESPONSE_CODE, Integer.class);

        httpEvent.setStatusCode(statusCode);
        httpEvent.setStatusMessage((statusCode != null) ? HttpStatus.valueOf(statusCode).getReasonPhrase() : null);

        return httpEvent;
    }
}
