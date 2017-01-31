package gov.hhs.onc.sdcct.ws.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.RestEvent;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import java.util.Map;
import javax.annotation.Nullable;

public interface WsEvent extends RestEvent {
    public boolean hasBindingName();

    @JsonProperty
    @Nullable
    public String getBindingName();

    public void setBindingName(@Nullable String bindingName);

    @JsonProperty
    public WsDirection getDirection();

    public void setDirection(WsDirection direction);

    @JsonProperty
    public String getEndpointAddress();

    public void setEndpointAddress(String endpointAddr);

    public boolean hasEndpointName();

    @JsonProperty
    @Nullable
    public String getEndpointName();

    public void setEndpointName(@Nullable String endpointName);

    public boolean hasMessageType();

    @JsonProperty
    @Nullable
    public WsMessageType getMessageType();

    public void setMessageType(@Nullable WsMessageType msgType);

    public boolean hasOperationName();

    @JsonProperty
    @Nullable
    public String getOperationName();

    public void setOperationName(@Nullable String opName);

    @JsonProperty
    public String getPayload();

    public void setPayload(String payload);

    public boolean hasPortName();

    @JsonProperty
    @Nullable
    public String getPortName();

    public void setPortName(@Nullable String portName);

    public boolean hasPortTypeName();

    @JsonProperty
    @Nullable
    public String getPortTypeName();

    public void setPortTypeName(@Nullable String portTypeName);

    public boolean hasPrettyPayload();

    @JsonProperty
    @Nullable
    public String getPrettyPayload();

    public void setPrettyPayload(@Nullable String prettyPayload);

    public boolean hasResourceName();

    @JsonProperty
    @Nullable
    public String getResourceName();

    public void setResourceName(@Nullable String resourceName);

    public boolean hasServiceName();

    @JsonProperty
    @Nullable
    public String getServiceName();

    public void setServiceName(@Nullable String serviceName);

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
