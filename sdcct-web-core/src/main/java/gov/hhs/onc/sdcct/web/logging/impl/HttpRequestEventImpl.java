package gov.hhs.onc.sdcct.web.logging.impl;

import gov.hhs.onc.sdcct.web.logging.HttpRequestEvent;
import javax.annotation.Nullable;
import org.springframework.http.HttpMethod;

public class HttpRequestEventImpl extends AbstractHttpEvent implements HttpRequestEvent {
    private String authType;
    private String contextPath;
    private String localName;
    private Integer localPort;
    private HttpMethod method;
    private String pathInfo;
    private String protocol;
    private String queryString;
    private String remoteAddr;
    private String remoteHost;
    private Integer remotePort;
    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String servletPath;
    private String uri;
    private String url;
    private String userPrincipal;

    @Nullable
    @Override
    public String getAuthType() {
        return this.authType;
    }

    @Override
    public void setAuthType(@Nullable String authType) {
        this.authType = authType;
    }

    @Nullable
    @Override
    public String getContextPath() {
        return this.contextPath;
    }

    @Override
    public void setContextPath(@Nullable String contextPath) {
        this.contextPath = contextPath;
    }

    @Nullable
    @Override
    public String getLocalName() {
        return this.localName;
    }

    @Override
    public void setLocalName(@Nullable String localName) {
        this.localName = localName;
    }

    @Nullable
    @Override
    public Integer getLocalPort() {
        return this.localPort;
    }

    @Override
    public void setLocalPort(@Nullable Integer localPort) {
        this.localPort = localPort;
    }

    @Override
    public HttpMethod getMethod() {
        return this.method;
    }

    @Override
    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
    public String getPathInfo() {
        return this.pathInfo;
    }

    @Override
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    @Nullable
    @Override
    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    @Override
    public void setRemoteAddr(@Nullable String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public String getRemoteHost() {
        return this.remoteHost;
    }

    @Override
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Override
    public Integer getRemotePort() {
        return this.remotePort;
    }

    @Override
    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Nullable
    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    @Nullable
    @Override
    public Integer getServerPort() {
        return this.serverPort;
    }

    @Override
    public void setServerPort(@Nullable Integer serverPort) {
        this.serverPort = serverPort;
    }

    @Nullable
    @Override
    public String getServletPath() {
        return this.servletPath;
    }

    @Override
    public void setServletPath(@Nullable String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public String getUserPrincipal() {
        return this.userPrincipal;
    }

    @Override
    public void setUserPrincipal(@Nullable String userPrincipal) {
        this.userPrincipal = userPrincipal;
    }
}
