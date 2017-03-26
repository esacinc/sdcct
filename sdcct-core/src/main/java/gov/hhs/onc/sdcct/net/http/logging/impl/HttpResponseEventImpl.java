package gov.hhs.onc.sdcct.net.http.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.net.http.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringBuilder;

@JsonTypeName("httpResponseEvent")
public class HttpResponseEventImpl extends AbstractHttpEvent implements HttpResponseEvent {
    private Integer statusCode;
    private String statusMsg;

    public HttpResponseEventImpl() {
        super(RestEventType.RESPONSE);
    }

    @Override
    protected void buildMarkerMessages(StringBuffer msgBuffer, SdcctToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        SdcctToStringBuilder logstashFileMsgToStrBuilder) {
        super.buildMarkerMessages(msgBuffer, msgToStrBuilder, logstashFileMsgBuffer, logstashFileMsgToStrBuilder);

        msgToStrBuilder.append("headers", this.headers);
        msgToStrBuilder.append("statusCode", this.statusCode);
        msgToStrBuilder.append("statusMsg", this.statusMsg);

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
