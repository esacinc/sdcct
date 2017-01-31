package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class ClientLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsRequestEvent, ClientLoggingOutCallback> {
    public ClientLoggingOutInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsRequestEvent.class, WsRequestEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
    }

    @Override
    protected ClientLoggingOutCallback buildCallback(Exchange exchange, Message msg, WsRequestEvent event, WsMessageType msgType) {
        return new ClientLoggingOutCallback(this.feature, exchange, msg, event, msgType);
    }
}
