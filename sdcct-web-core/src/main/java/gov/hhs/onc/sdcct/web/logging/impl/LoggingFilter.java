package gov.hhs.onc.sdcct.web.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpRequestEventImpl;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpResponseEventImpl;
import gov.hhs.onc.sdcct.web.filter.impl.AbstractSdcctFilter;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponse;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("filterLogging")
public class LoggingFilter extends AbstractSdcctFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(TomcatRequest servletReq, TomcatResponse servletResp, FilterChain chain) throws IOException, ServletException {
        String txId = ((String) servletReq.getAttribute(SdcctPropertyNames.HTTP_SERVER_TX_ID));

        HttpRequestEvent reqEvent = new HttpRequestEventImpl();
        reqEvent.setAuthType(servletReq.getAuthType());
        reqEvent.setCharacterEncoding(servletReq.getCharacterEncoding());
        reqEvent.setContentLength(servletReq.getContentLengthLong());
        reqEvent.setContentType(servletReq.getContentType());
        reqEvent.setContextPath(servletReq.getContextPath());
        reqEvent.setEndpointType(RestEndpointType.SERVER);
        reqEvent.setHeaders(servletReq.getHeaders());
        reqEvent.setLocale(servletReq.getLocale());
        reqEvent.setLocalName(servletReq.getLocalName());
        reqEvent.setLocalPort(servletReq.getLocalPort());
        reqEvent.setMethod(servletReq.getMethod());
        reqEvent.setPathInfo(servletReq.getPathInfo());
        reqEvent.setProtocol(servletReq.getProtocol());
        reqEvent.setQueryParameters(servletReq.getParameters());
        reqEvent.setQueryString(servletReq.getQueryString());
        reqEvent.setRemoteAddr(servletReq.getRemoteAddr());
        reqEvent.setRemoteHost(servletReq.getRemoteHost());
        reqEvent.setRemotePort(servletReq.getRemotePort());
        reqEvent.setScheme(servletReq.getScheme());
        reqEvent.setSecure(servletReq.isSecure());
        reqEvent.setServerName(servletReq.getServerName());
        reqEvent.setServerPort(servletReq.getServerPort());
        reqEvent.setServletPath(servletReq.getServletPath());
        reqEvent.setTxId(txId);
        reqEvent.setUri(servletReq.getRequestURI());
        reqEvent.setUrl(servletReq.getRequestURL().toString());
        reqEvent.setUserPrincipal(Objects.toString(servletReq.getUserPrincipal(), null));

        LOGGER.info(reqEvent.buildMarker(), StringUtils.EMPTY);

        try {
            chain.doFilter(servletReq, servletResp);
        } finally {
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

            LOGGER.info(respEvent.buildMarker(), StringUtils.EMPTY);
        }
    }
}
