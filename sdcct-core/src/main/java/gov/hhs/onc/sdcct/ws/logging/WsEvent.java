package gov.hhs.onc.sdcct.ws.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.ws.WsDirection;
import java.util.Map;
import javax.annotation.Nullable;

public interface WsEvent extends RestEvent {
    @JsonProperty
    public WsDirection getDirection();

    public void setDirection(WsDirection direction);

    @JsonProperty
    public String getEndpointAddress();

    public void setEndpointAddress(String endpointAddr);

    @JsonProperty
    public String getPayload();

    public void setPayload(String payload);

    public boolean hasPrettyPayload();

    @JsonProperty
    @Nullable
    public String getPrettyPayload();

    public void setPrettyPayload(@Nullable String prettyPayload);

    public boolean hasSoapFault();

    @JsonProperty
    @Nullable
    public Map<String, Object> getSoapFault();

    public void setSoapFault(@Nullable Map<String, Object> soapFault);

    public boolean hasSoapHeaders();

    @JsonProperty
    @Nullable
    public Map<String, Object> getSoapHeaders();

    public void setSoapHeaders(@Nullable Map<String, Object> soapHeaders);
}
