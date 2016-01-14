package gov.hhs.onc.sdcct.web.logging.impl;

import gov.hhs.onc.sdcct.logging.impl.AbstractLoggingEvent;
import gov.hhs.onc.sdcct.web.logging.HttpEvent;
import javax.annotation.Nullable;
import org.springframework.http.HttpHeaders;

public abstract class AbstractHttpEvent extends AbstractLoggingEvent implements HttpEvent {
    protected Long contentLen;
    protected String contentType;
    protected HttpHeaders headers = new HttpHeaders();

    @Nullable
    public Long getContentLength() {
        return this.contentLen;
    }

    public void setContentLength(@Nullable Long contentLen) {
        this.contentLen = contentLen;
    }

    @Nullable
    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(@Nullable String contentType) {
        this.contentType = contentType;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }
}
