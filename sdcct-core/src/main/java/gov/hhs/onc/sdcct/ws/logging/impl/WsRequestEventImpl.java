package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;

public class WsRequestEventImpl extends AbstractWsEvent implements WsRequestEvent {
    public WsRequestEventImpl() {
        super(RestEventType.REQUEST);
    }
}
