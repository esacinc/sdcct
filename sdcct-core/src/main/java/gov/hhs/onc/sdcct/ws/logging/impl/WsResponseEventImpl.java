package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;

public class WsResponseEventImpl extends AbstractWsEvent implements WsResponseEvent {
    public WsResponseEventImpl() {
        super(RestEventType.RESPONSE);
    }
}
