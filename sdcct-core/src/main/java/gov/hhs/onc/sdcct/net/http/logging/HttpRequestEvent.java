package gov.hhs.onc.sdcct.net.http.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public interface HttpRequestEvent extends HttpEvent {
    public boolean hasAuthType();

    @JsonProperty
    @Nullable
    public String getAuthType();

    public void setAuthType(@Nullable String authType);

    public boolean hasContextPath();

    @JsonProperty
    @Nullable
    public String getContextPath();

    public void setContextPath(@Nullable String contextPath);

    public boolean hasLocalName();

    @JsonProperty
    @Nullable
    public String getLocalName();

    public void setLocalName(@Nullable String localName);

    public boolean hasLocalPort();

    @JsonProperty
    @Nullable
    public Integer getLocalPort();

    public void setLocalPort(@Nullable Integer localPort);

    @JsonProperty
    public String getMethod();

    public void setMethod(String method);

    @JsonProperty
    public String getPathInfo();

    public void setPathInfo(String pathInfo);

    @JsonProperty
    public String getProtocol();

    public void setProtocol(String protocol);

    @JsonProperty
    public Map<String, List<String>> getQueryParameters();

    public void setQueryParameters(Map<String, List<String>> queryParams);

    @JsonProperty
    public String getQueryString();

    public void setQueryString(String queryStr);

    public boolean hasRemoteAddr();

    @JsonProperty
    @Nullable
    public String getRemoteAddr();

    public void setRemoteAddr(@Nullable String remoteAddr);

    @JsonProperty
    public String getRemoteHost();

    public void setRemoteHost(String remoteHost);

    @JsonProperty
    public Integer getRemotePort();

    public void setRemotePort(Integer remotePort);

    @JsonProperty
    public String getScheme();

    public void setScheme(String scheme);

    @JsonProperty
    public boolean isSecure();

    public void setSecure(boolean secure);

    public boolean hasServerName();

    @JsonProperty
    @Nullable
    public String getServerName();

    public void setServerName(@Nullable String serverName);

    public boolean hasServerPort();

    @JsonProperty
    @Nullable
    public Integer getServerPort();

    public void setServerPort(@Nullable Integer serverPort);

    public boolean hasServletPath();

    @JsonProperty
    @Nullable
    public String getServletPath();

    public void setServletPath(@Nullable String servletPath);

    @JsonProperty
    public String getUri();

    public void setUri(String uri);

    @JsonProperty
    public String getUrl();

    public void setUrl(String url);

    public boolean hasUserPrincipal();

    @JsonProperty
    @Nullable
    public String getUserPrincipal();

    public void setUserPrincipal(@Nullable String userPrincipal);
}
