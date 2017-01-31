package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class ServerLoggingOutInterceptor extends AbstractLoggingOutInterceptor<WsResponseEvent, ServerLoggingOutCallback> {
    public ServerLoggingOutInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.SERVER, SdcctPropertyNames.WS_SERVER_TX_ID, msgType);
    }

    @Override
    protected ServerLoggingOutCallback buildCallback(Exchange exchange, Message msg, WsResponseEvent event, WsMessageType msgType) {
        return new ServerLoggingOutCallback(this.feature, exchange, msg, event, msgType);
    }
}
