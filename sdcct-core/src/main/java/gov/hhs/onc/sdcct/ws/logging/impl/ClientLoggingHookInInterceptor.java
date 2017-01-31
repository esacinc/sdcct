package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;

public class ClientLoggingHookInInterceptor extends AbstractLoggingHookInInterceptor<WsResponseEvent> {
    public ClientLoggingHookInInterceptor(SdcctLoggingFeature feature, WsMessageType msgType) {
        super(feature, WsResponseEvent.class, WsResponseEventImpl::new, RestEndpointType.CLIENT, SdcctPropertyNames.WS_CLIENT_TX_ID, msgType);
    }
}
