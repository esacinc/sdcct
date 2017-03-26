package gov.hhs.onc.sdcct.web.logging.impl;

import gov.hhs.onc.sdcct.net.http.logging.HttpRequestEvent;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.web.filter.impl.AbstractSdcctFilter;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponse;
import gov.hhs.onc.sdcct.web.tomcat.utils.SdcctHttpEventUtils;
import java.io.IOException;
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
        HttpRequestEvent reqEvent = SdcctHttpEventUtils.createHttpRequestEvent(servletReq);

        LOGGER.info(reqEvent.buildMarker(), StringUtils.EMPTY);

        try {
            chain.doFilter(servletReq, servletResp);
        } finally {
            HttpResponseEvent respEvent = SdcctHttpEventUtils.createHttpResponseEvent(servletReq, servletResp);

            LOGGER.info(respEvent.buildMarker(), StringUtils.EMPTY);
        }
    }
}
