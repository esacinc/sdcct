package gov.hhs.onc.sdcct.net.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.HttpEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractHttpEvent extends AbstractRestEvent implements HttpEvent {
    protected String charEnc;
    protected Long contentLen;
    protected String contentType;
    protected Map<String, List<String>> headers;
    protected Locale locale;

    protected AbstractHttpEvent(RestEventType eventType) {
        super(eventType);
    }

    @Override
    protected void buildMarkerMessages(StringBuffer msgBuffer, ToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        ToStringBuilder logstashFileMsgToStrBuilder) {
        msgBuffer.append("HTTP ");
        msgBuffer.append(this.endpointType.getId());
        msgBuffer.append(StringUtils.SPACE);
        msgBuffer.append(this.eventType.getId());

        logstashFileMsgBuffer.append(msgBuffer);
        logstashFileMsgBuffer.append(SdcctStringUtils.PERIOD_CHAR);

        msgBuffer.append(" (");

        msgToStrBuilder.append("endpointType", this.endpointType.getId());
    }

    @Override
    protected String buildMarkerFieldName() {
        return ("http" + StringUtils.capitalize(this.endpointType.getId()) + StringUtils.capitalize(this.eventType.getId()));
    }

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
