package gov.hhs.onc.sdcct.net.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.HttpEvent;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class AbstractHttpEvent extends AbstractRestEvent implements HttpEvent {
    protected String charEnc;
    protected Long contentLen;
    protected String contentType;
    protected Map<String, List<String>> headers;
    protected Locale locale;

    @JsonProperty
    @Override
    public String getCharacterEncoding() {
        return this.charEnc;
    }

    @Override
    public void setCharacterEncoding(String charEnc) {
        this.charEnc = charEnc;
    }

    @Override
    public boolean hasContentLength() {
        return (this.contentLen != null);
    }

    @JsonProperty
    @Nullable
    public Long getContentLength() {
        return this.contentLen;
    }

    public void setContentLength(@Nullable Long contentLen) {
        this.contentLen = contentLen;
    }

    @Override
    public boolean hasContentType() {
        return (this.contentType != null);
    }

    @JsonProperty
    @Nullable
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty
    @Override
    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    @Override
    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    @JsonProperty
    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
