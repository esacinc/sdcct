package gov.hhs.onc.sdcct.net.http.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class HttpResponseEventImpl extends AbstractHttpEvent implements HttpResponseEvent {
    private Integer statusCode;
    private String statusMsg;

    public HttpResponseEventImpl() {
        super(RestEventType.RESPONSE);
    }

    @Override
    protected void buildMarkerMessages(StringBuffer msgBuffer, ToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        ToStringBuilder logstashFileMsgToStrBuilder) {
        super.buildMarkerMessages(msgBuffer, msgToStrBuilder, logstashFileMsgBuffer, logstashFileMsgToStrBuilder);

        msgToStrBuilder.append("headers", this.headers);
        msgToStrBuilder.append("statusCode", this.statusCode);
        msgToStrBuilder.append("statusMsg", this.statusMsg);

        ((SdcctToStringStyle) msgToStrBuilder.getStyle()).removeLastFieldSeparator(msgBuffer);

        msgBuffer.append(").");
    }

    @JsonProperty
    @Override
    public Integer getStatusCode() {
        return this.statusCode;
    }

    @Override
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty
    @Override
    public String getStatusMessage() {
        return this.statusMsg;
    }

    @Override
    public void setStatusMessage(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
