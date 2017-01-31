package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.Headers;

public class ClientLoggingProcessInInterceptor extends AbstractLoggingProcessInInterceptor<WsResponseEvent> {
    public ClientLoggingProcessInInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
    }

    @Override
    protected void handleMessageInternal(Exchange exchange, Message msg, WsResponseEvent event) throws Exception {
        HttpResponseEvent httpEvent = new HttpResponseEventImpl();

        SdcctLoggingFeature.buildTxId(exchange, msg, httpEvent, SdcctPropertyNames.HTTP_CLIENT_TX_ID);

        httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
        httpEvent.setEndpointType(RestEndpointType.CLIENT);
        httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
        httpEvent.setStatusCode(SdcctWsPropertyUtils.getProperty(msg, Message.RESPONSE_CODE, Integer.class));

        this.feature.logEvent(httpEvent);

        super.handleMessageInternal(exchange, msg, event);
    }
}
