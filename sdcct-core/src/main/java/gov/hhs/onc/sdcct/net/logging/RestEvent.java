package gov.hhs.onc.sdcct.net.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.logging.LoggingEvent;

public interface RestEvent extends LoggingEvent {
    @JsonProperty
    public RestEndpointType getEndpointType();

    public void setEndpointType(RestEndpointType endpointType);

    @JsonProperty
    public String getTxId();

    public void setTxId(String txId);
}
