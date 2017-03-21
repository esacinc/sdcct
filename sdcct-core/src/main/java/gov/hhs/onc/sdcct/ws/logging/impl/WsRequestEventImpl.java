package gov.hhs.onc.sdcct.ws.logging.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;

@JsonTypeName("wsRequestEvent")
public class WsRequestEventImpl extends AbstractWsEvent implements WsRequestEvent {
    public WsRequestEventImpl() {
        super(RestEventType.REQUEST);
    }
}
