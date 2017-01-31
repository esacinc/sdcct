package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.util.function.Supplier;

public abstract class AbstractLoggingInInterceptor<T extends WsEvent> extends AbstractLoggingInterceptor<T> {
    protected AbstractLoggingInInterceptor(SdcctLoggingFeature feature, String phase, Class<T> eventClass, Supplier<T> eventCreator,
        RestEndpointType endpointType, String txIdPropName, WsMessageType msgType) {
        super(feature, phase, eventClass, eventCreator, WsDirection.INBOUND, endpointType, txIdPropName, msgType);
    }
}
