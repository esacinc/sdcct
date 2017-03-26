package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctRestEventUtils;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class ClientLoggingProcessInInterceptor extends AbstractLoggingProcessInInterceptor<WsResponseEvent> {
    public ClientLoggingProcessInInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
    }

    @Override
    protected void handleMessageInternal(Exchange exchange, Message msg, WsResponseEvent event) throws Exception {
        HttpResponseEvent httpEvent = SdcctRestEventUtils.createHttpResponseEvent(msg, SdcctPropertyNames.HTTP_CLIENT_TX_ID, RestEndpointType.CLIENT);

        this.feature.logEvent(httpEvent);

        super.handleMessageInternal(exchange, msg, event);
    }
}
