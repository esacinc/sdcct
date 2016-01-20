package gov.hhs.onc.sdcct.web.tomcat.utils;

import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequestFacade;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponse;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatResponseFacade;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public final class SdcctTomcatUtils {
    private SdcctTomcatUtils() {
    }

    public static TomcatResponse unwrapResponse(HttpServletResponse servletResp) {
        return ((TomcatResponseFacade) unwrapResponseInternal(servletResp)).getResponse();
    }

    public static TomcatRequest unwrapRequest(HttpServletRequest servletReq) {
        return ((TomcatRequestFacade) unwrapRequestInternal(servletReq)).getRequest();
    }

    private static HttpServletResponse unwrapResponseInternal(HttpServletResponse servletResp) {
        return ((servletResp instanceof HttpServletResponseWrapper)
            ? unwrapResponse(((HttpServletResponse) ((HttpServletResponseWrapper) servletResp).getResponse())) : servletResp);
    }

    private static HttpServletRequest unwrapRequestInternal(HttpServletRequest servletReq) {
        return ((servletReq instanceof HttpServletRequestWrapper)
            ? unwrapRequest(((HttpServletRequest) ((HttpServletRequestWrapper) servletReq).getRequest())) : servletReq);
    }
}
