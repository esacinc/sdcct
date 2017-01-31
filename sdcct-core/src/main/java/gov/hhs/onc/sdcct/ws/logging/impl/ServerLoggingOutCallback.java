package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class ServerLoggingOutCallback extends AbstractLoggingOutCallback<WsResponseEvent> {
    public ServerLoggingOutCallback(SdcctLoggingFeature feature, Exchange exchange, Message msg, WsResponseEvent event, WsMessageType msgType) {
        super(feature, exchange, msg, event, msgType);
    }
}
