package gov.hhs.onc.sdcct.web.filter.impl;

import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponse;
import gov.hhs.onc.sdcct.web.tomcat.utils.SdcctTomcatUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractSdcctFilter implements Filter {
    protected FilterConfig config;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletReq, ServletResponse servletResp, FilterChain chain) throws IOException, ServletException {
        this.doFilterInternal(SdcctTomcatUtils.unwrapRequest(((HttpServletRequest) servletReq)),
            SdcctTomcatUtils.unwrapResponse(((HttpServletResponse) servletResp)), chain);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    protected abstract void doFilterInternal(TomcatRequest servletReq, TomcatResponse servletResp, FilterChain chain) throws IOException, ServletException;
}
