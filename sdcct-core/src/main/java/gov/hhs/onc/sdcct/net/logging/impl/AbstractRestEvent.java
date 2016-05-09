package gov.hhs.onc.sdcct.net.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.logging.impl.AbstractLoggingEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.net.logging.RestEventType;

public abstract class AbstractRestEvent extends AbstractLoggingEvent implements RestEvent {
    protected RestEventType eventType;
    protected RestEndpointType endpointType;
    protected String txId;

    protected AbstractRestEvent(RestEventType eventType) {
        this.eventType = eventType;
    }

    @JsonProperty
    @Override
    public RestEndpointType getEndpointType() {
        return this.endpointType;
    }

    @Override
    public void setEndpointType(RestEndpointType endpointType) {
        this.endpointType = endpointType;
    }

    @JsonProperty
    @Override
    public RestEventType getEventType() {
        return this.eventType;
    }

    @JsonProperty
    @Override
    public String getTxId() {
        return this.txId;
    }

    @Override
    public void setTxId(String txId) {
        this.txId = txId;
    }
}
