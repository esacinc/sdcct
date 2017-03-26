package gov.hhs.onc.sdcct.web.tomcat.utils;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponse;
import java.util.Objects;

public class SdcctHttpEventUtils {
    private SdcctHttpEventUtils() {
    }

    public static HttpRequestEvent createHttpRequestEvent(TomcatRequest servletReq) {
        String txId = ((String) servletReq.getAttribute(SdcctPropertyNames.HTTP_SERVER_TX_ID));

        HttpRequestEvent httpReqEvent = new HttpRequestEventImpl();
        // noinspection ConstantConditions
        httpReqEvent.setAuthType(servletReq.getAuthType());
        httpReqEvent.setCharacterEncoding(servletReq.getCharacterEncoding());
        httpReqEvent.setContentLength(servletReq.getContentLengthLong());
        httpReqEvent.setContentType(servletReq.getContentType());
        httpReqEvent.setContextPath(servletReq.getContextPath());
        httpReqEvent.setEndpointType(RestEndpointType.SERVER);
        httpReqEvent.setHeaders(servletReq.getHeaders());
        httpReqEvent.setLocale(servletReq.getLocale());
        httpReqEvent.setLocalName(servletReq.getLocalName());
        httpReqEvent.setLocalPort(servletReq.getLocalPort());
        httpReqEvent.setMethod(servletReq.getMethod());
        httpReqEvent.setPathInfo(servletReq.getPathInfo());
        httpReqEvent.setProtocol(servletReq.getProtocol());
        httpReqEvent.setQueryParameters(servletReq.getParameters());
        httpReqEvent.setQueryString(servletReq.getQueryString());
        httpReqEvent.setRemoteAddr(servletReq.getRemoteAddr());
        httpReqEvent.setRemoteHost(servletReq.getRemoteHost());
        httpReqEvent.setRemotePort(servletReq.getRemotePort());
        httpReqEvent.setScheme(servletReq.getScheme());
        httpReqEvent.setSecure(servletReq.isSecure());
        httpReqEvent.setServerName(servletReq.getServerName());
        httpReqEvent.setServerPort(servletReq.getServerPort());
        httpReqEvent.setServletPath(servletReq.getServletPath());
        httpReqEvent.setTxId(txId);
        httpReqEvent.setUri(servletReq.getRequestURI());
        httpReqEvent.setUrl(servletReq.getRequestURL().toString());
        httpReqEvent.setUserPrincipal(Objects.toString(servletReq.getUserPrincipal(), null));

        return httpReqEvent;
    }

    public static HttpResponseEvent createHttpResponseEvent(TomcatRequest servletReq, TomcatResponse servletResp) {
        String txId = ((String) servletReq.getAttribute(SdcctPropertyNames.HTTP_SERVER_TX_ID));

        HttpResponseEvent respEvent = new HttpResponseEventImpl();
        respEvent.setCharacterEncoding(servletResp.getCharacterEncoding());
        respEvent.setContentLength(servletResp.getContentLengthLong());
        respEvent.setContentType(servletResp.getContentType());
        respEvent.setEndpointType(RestEndpointType.SERVER);
        respEvent.setHeaders(servletResp.getHeaders());
        respEvent.setLocale(servletResp.getLocale());
        respEvent.setStatusCode(servletResp.getStatus());
        respEvent.setStatusMessage(servletResp.getMessage());
        respEvent.setTxId(txId);

        return respEvent;
    }
}
