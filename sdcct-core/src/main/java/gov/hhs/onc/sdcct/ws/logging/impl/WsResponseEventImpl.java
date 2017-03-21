package gov.hhs.onc.sdcct.ws.logging.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;

@JsonTypeName("wsResponseEvent")
public class WsResponseEventImpl extends AbstractWsEvent implements WsResponseEvent {
    public WsResponseEventImpl() {
        super(RestEventType.RESPONSE);
    }
}
