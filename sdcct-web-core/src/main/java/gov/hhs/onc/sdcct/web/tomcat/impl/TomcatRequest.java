package gov.hhs.onc.sdcct.web.tomcat.impl;

import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.catalina.connector.Request;
import org.apache.commons.collections4.EnumerationUtils;

public class TomcatRequest extends Request {
    public Map<String, List<String>> getHeaders() {
        return EnumerationUtils.toList(this.getHeaderNames()).stream().collect(SdcctStreamUtils.toMap(Function.identity(),
            headerName -> EnumerationUtils.toList(this.getHeaders(headerName)), () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)));
    }

    public Map<String, List<String>> getParameters() {
        return EnumerationUtils.toList(this.getParameterNames()).stream().collect(SdcctStreamUtils.toMap(Function.identity(),
            paramName -> Stream.of(this.getParameterValues(paramName)).collect(Collectors.toList()), () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)));
    }

    @Override
    public TomcatRequestFacade getRequest() {
        return ((TomcatRequestFacade) ((this.facade != null) ? this.facade : (this.facade = new TomcatRequestFacade(this))));
    }
}
