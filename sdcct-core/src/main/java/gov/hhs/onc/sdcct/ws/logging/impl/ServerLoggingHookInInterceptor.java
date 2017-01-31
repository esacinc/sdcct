package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;

public class ServerLoggingHookInInterceptor extends AbstractLoggingHookInInterceptor<WsRequestEvent> {
    public ServerLoggingHookInInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsRequestEvent.class, WsRequestEventImpl::new, RestEndpointType.SERVER, SdcctPropertyNames.WS_SERVER_TX_ID, msgType);
    }
}
