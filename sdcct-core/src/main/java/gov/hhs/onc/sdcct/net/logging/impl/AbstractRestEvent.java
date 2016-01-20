package gov.hhs.onc.sdcct.net.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.logging.impl.AbstractLoggingEvent;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.net.logging.RestEvent;

public abstract class AbstractRestEvent extends AbstractLoggingEvent implements RestEvent {
    protected RestEndpointType endpointType;
    protected String txId;

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
    public String getTxId() {
        return this.txId;
    }

    @Override
    public void setTxId(String txId) {
        this.txId = txId;
    }
}
