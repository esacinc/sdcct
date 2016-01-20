package gov.hhs.onc.sdcct.net.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.logging.impl.SdcctMarkerBuilder;
import gov.hhs.onc.sdcct.net.logging.HttpResponseEvent;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public class HttpResponseEventImpl extends AbstractHttpEvent implements HttpResponseEvent {
    private Integer statusCode;
    private String statusMsg;

    @Override
    protected SdcctMarkerBuilder buildMarkerInternal() {
        String msgPrefix = String.format("HTTP %s response", this.endpointType.name().toLowerCase());

        return super.buildMarkerInternal().appendMessage(
            (msgPrefix + String.format(" (txId=%s, headers=%s, statusCode=%d, statusMsg=%s).", this.txId, this.headers, this.statusCode, this.statusMsg)),
            (msgPrefix + SdcctStringUtils.PERIOD_CHAR));
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
