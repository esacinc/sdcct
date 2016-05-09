package gov.hhs.onc.sdcct.net.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

public interface HttpEvent extends RestEvent {
    @JsonProperty
    public String getCharacterEncoding();

    public void setCharacterEncoding(String charEnc);

    public boolean hasContentLength();

    @JsonProperty
    @Nullable
    public Long getContentLength();

    public void setContentLength(@Nullable Long contentLen);

    public boolean hasContentType();

    @JsonProperty
    @Nullable
    public String getContentType();

    public void setContentType(@Nullable String contentType);

    @JsonProperty
    public Map<String, List<String>> getHeaders();

    public void setHeaders(Map<String, List<String>> headers);

    @JsonProperty
    public Locale getLocale();

    public void setLocale(Locale locale);
}
