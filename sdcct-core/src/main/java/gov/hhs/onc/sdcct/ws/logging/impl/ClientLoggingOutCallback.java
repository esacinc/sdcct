package gov.hhs.onc.sdcct.ws.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.Headers;

public class ClientLoggingOutCallback extends AbstractLoggingOutCallback<WsRequestEvent> {
    public ClientLoggingOutCallback(SdcctLoggingFeature feature, Exchange exchange, Message msg, WsRequestEvent event, WsMessageType msgType) {
        super(feature, exchange, msg, event, msgType);
    }

    @Override
    protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
        super.onCloseInternal(cachedStream);

        HttpRequestEvent httpEvent = new HttpRequestEventImpl();

        SdcctLoggingFeature.buildTxId(exchange, msg, httpEvent, SdcctPropertyNames.HTTP_CLIENT_TX_ID);

        httpEvent.setContentType(SdcctWsPropertyUtils.getProperty(msg, Message.CONTENT_TYPE));
        httpEvent.setEndpointType(RestEndpointType.CLIENT);
        httpEvent.setHeaders(Headers.getSetProtocolHeaders(msg));
        httpEvent.setMethod(SdcctWsPropertyUtils.getProperty(msg, Message.HTTP_REQUEST_METHOD));
        httpEvent.setPathInfo(SdcctWsPropertyUtils.getProperty(msg, Message.PATH_INFO));
        httpEvent.setQueryString(SdcctWsPropertyUtils.getProperty(msg, Message.QUERY_STRING));
        httpEvent.setScheme(SdcctWsPropertyUtils.getProperty(msg, WsPropertyNames.HTTP_SCHEME));
        httpEvent.setUri(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URI));
        httpEvent.setUrl(SdcctWsPropertyUtils.getProperty(msg, Message.REQUEST_URL));

        this.feature.logEvent(httpEvent);
    }
}
