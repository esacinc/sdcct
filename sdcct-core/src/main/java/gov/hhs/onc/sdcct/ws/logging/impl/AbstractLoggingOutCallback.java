package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.io.OutputStream;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public abstract class AbstractLoggingOutCallback<T extends WsEvent> implements CachedOutputStreamCallback {
    protected SdcctLoggingFeature feature;
    protected Exchange exchange;
    protected Message msg;
    protected T event;
    protected WsMessageType msgType;

    protected AbstractLoggingOutCallback(SdcctLoggingFeature feature, Exchange exchange, Message msg, T event, WsMessageType msgType) {
        this.feature = feature;
        this.exchange = exchange;
        this.msg = msg;
        this.event = event;
        this.msgType = msgType;
    }

    @Override
    public void onClose(CachedOutputStream cachedStream) {
        try {
            this.onCloseInternal(((CacheAndWriteOutputStream) cachedStream));
        } catch (Fault e) {
            throw e;
        } catch (Exception e) {
            throw new Fault(e);
        }
    }

    @Override
    public void onFlush(CachedOutputStream cachedStream) {
    }

    protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
        try {
            this.feature.processEvent(this.exchange, this.msg, this.event, this.msgType, cachedStream.getBytes());
        } finally {
            OutputStream outStream = cachedStream.getFlowThroughStream();

            cachedStream.lockOutputStream();
            cachedStream.resetOut(null, false);

            this.msg.setContent(OutputStream.class, outStream);
        }
    }
}
