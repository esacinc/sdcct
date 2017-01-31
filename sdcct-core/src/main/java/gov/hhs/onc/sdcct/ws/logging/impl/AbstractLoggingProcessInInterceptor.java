package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.util.function.Supplier;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

public abstract class AbstractLoggingProcessInInterceptor<T extends WsEvent> extends AbstractLoggingInInterceptor<T> {
    protected AbstractLoggingProcessInInterceptor(SdcctLoggingFeature feature, Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType,
        String txIdPropName, WsMessageType msgType) {
        super(feature, Phase.PRE_INVOKE, eventClass, eventCreator, endpointType, txIdPropName, msgType);
    }

    @Override
    protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
        CachedOutputStream cachedStream = msg.getContent(CachedOutputStream.class);
        cachedStream.close();

        this.feature.processEvent(exchange, msg, event, this.msgType, cachedStream.getBytes());
    }
}
