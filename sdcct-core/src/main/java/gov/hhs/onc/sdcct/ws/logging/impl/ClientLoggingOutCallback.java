package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctRestEventUtils;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;

public class ClientLoggingOutCallback extends AbstractLoggingOutCallback<WsRequestEvent> {
    public ClientLoggingOutCallback(SdcctLoggingFeature feature, Exchange exchange, Message msg, WsRequestEvent event, WsMessageType msgType) {
        super(feature, exchange, msg, event, msgType);
    }

    @Override
    protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
        super.onCloseInternal(cachedStream);

        HttpRequestEvent httpEvent = SdcctRestEventUtils.createHttpRequestEvent(msg, SdcctPropertyNames.HTTP_CLIENT_TX_ID, RestEndpointType.CLIENT);

        this.feature.logEvent(httpEvent);
    }
}
