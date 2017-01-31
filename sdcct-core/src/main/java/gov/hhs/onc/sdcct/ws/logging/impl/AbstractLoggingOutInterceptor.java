package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.io.OutputStream;
import java.util.function.Supplier;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public abstract class AbstractLoggingOutInterceptor<T extends WsEvent, U extends AbstractLoggingOutCallback<T>> extends AbstractLoggingInterceptor<T> {
    protected AbstractLoggingOutInterceptor(SdcctLoggingFeature feature, Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType,
        String txIdPropName, WsMessageType msgType) {
        super(feature, Phase.PRE_STREAM, eventClass, eventCreator, WsDirection.OUTBOUND, endpointType, txIdPropName, msgType);

        this.addBefore(StaxOutInterceptor.class.getName());
    }

    @Override
    protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
        CacheAndWriteOutputStream cachedStream = new CacheAndWriteOutputStream(msg.getContent(OutputStream.class));
        cachedStream.registerCallback(this.buildCallback(exchange, msg, event, msgType));

        msg.setContent(OutputStream.class, cachedStream);
    }

    protected abstract U buildCallback(Exchange exchange, Message msg, T event, WsMessageType msgType);
}
