package gov.hhs.onc.sdcct.web.logging;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import javax.annotation.Nullable;
import org.springframework.http.HttpHeaders;

public interface HttpEvent extends LoggingEvent {
    @Nullable
    public Long getContentLength();

    public void setContentLength(@Nullable Long contentLen);

    @Nullable
    public String getContentType();

    public void setContentType(@Nullable String contentType);

    public HttpHeaders getHeaders();
}
