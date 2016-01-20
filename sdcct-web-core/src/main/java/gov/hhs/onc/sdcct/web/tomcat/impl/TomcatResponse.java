package gov.hhs.onc.sdcct.web.tomcat.impl;

import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.apache.catalina.connector.Response;

public class TomcatResponse extends Response {
    public long getContentLengthLong() {
        return this.coyoteResponse.getContentLengthLong();
    }

    public Map<String, List<String>> getHeaders() {
        return this.getHeaderNames().stream().collect(SdcctStreamUtils.toMap(Function.identity(), headerName -> new ArrayList<>(this.getHeaders(headerName)),
            () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)));
    }

    @Override
    public TomcatResponseFacade getResponse() {
        return ((TomcatResponseFacade) ((this.facade != null) ? this.facade : (this.facade = new TomcatResponseFacade(this))));
    }
}
