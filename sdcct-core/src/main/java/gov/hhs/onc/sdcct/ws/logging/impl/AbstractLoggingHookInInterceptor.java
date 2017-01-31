package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.function.Supplier;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.DelegatingInputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.PolicyConstants;

public abstract class AbstractLoggingHookInInterceptor<T extends WsEvent> extends AbstractLoggingInInterceptor<T> {
    protected AbstractLoggingHookInInterceptor(SdcctLoggingFeature feature, Class<T> eventClass, Supplier<T> eventCreator, RestEndpointType endpointType,
        String txIdPropName, WsMessageType msgType) {
        super(feature, Phase.RECEIVE, eventClass, eventCreator, endpointType, txIdPropName, msgType);

        this.addBefore(PolicyConstants.POLICY_IN_INTERCEPTOR_ID);
    }

    @Override
    protected void handleMessageInternal(Exchange exchange, Message msg, T event) throws Exception {
        InputStream inStream = msg.getContent(InputStream.class), targetInStream;
        boolean delegatingInStreamAvailable = (inStream instanceof DelegatingInputStream);
        DelegatingInputStream delegatingInStream = (delegatingInStreamAvailable ? ((DelegatingInputStream) inStream) : null);
        CachedOutputStream cachedStream = new CachedOutputStream();

        IOUtils.copyAtLeast((targetInStream = (delegatingInStreamAvailable ? delegatingInStream.getInputStream() : inStream)), cachedStream,
            Integer.MAX_VALUE);

        cachedStream.flush();

        targetInStream = new SequenceInputStream(cachedStream.getInputStream(), targetInStream);

        if (delegatingInStreamAvailable) {
            delegatingInStream.setInputStream(targetInStream);
        } else {
            msg.setContent(InputStream.class, targetInStream);
        }

        msg.setContent(CachedOutputStream.class, cachedStream);
    }
}
