package gov.hhs.onc.sdcct.web.logging;

import gov.hhs.onc.sdcct.logging.MarkerFieldName;
import javax.annotation.Nullable;
import org.springframework.http.HttpMethod;

@MarkerFieldName("httpRequest")
public interface HttpRequestEvent extends HttpEvent {
    @Nullable
    public String getAuthType();

    public void setAuthType(@Nullable String authType);

    @Nullable
    public String getContextPath();

    public void setContextPath(@Nullable String contextPath);

    @Nullable
    public String getLocalName();

    public void setLocalName(@Nullable String localName);

    @Nullable
    public Integer getLocalPort();

    public void setLocalPort(@Nullable Integer localPort);

    public HttpMethod getMethod();

    public void setMethod(HttpMethod method);

    public String getPathInfo();

    public void setPathInfo(String pathInfo);

    public String getProtocol();

    public void setProtocol(String protocol);

    public String getQueryString();

    public void setQueryString(String queryStr);

    @Nullable
    public String getRemoteAddr();

    public void setRemoteAddr(@Nullable String remoteAddr);

    public String getRemoteHost();

    public void setRemoteHost(String remoteHost);

    public Integer getRemotePort();

    public void setRemotePort(Integer remotePort);

    public String getScheme();

    public void setScheme(String scheme);

    @Nullable
    public String getServerName();

    public void setServerName(@Nullable String serverName);

    @Nullable
    public Integer getServerPort();

    public void setServerPort(@Nullable Integer serverPort);

    @Nullable
    public String getServletPath();

    public void setServletPath(@Nullable String servletPath);

    public String getUri();

    public void setUri(String uri);

    public String getUrl();

    public void setUrl(String url);

    @Nullable
    public String getUserPrincipal();

    public void setUserPrincipal(@Nullable String userPrincipal);
}
