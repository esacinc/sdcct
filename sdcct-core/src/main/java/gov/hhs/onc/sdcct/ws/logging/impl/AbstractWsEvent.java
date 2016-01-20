package gov.hhs.onc.sdcct.ws.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.impl.AbstractRestEvent;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import java.util.Map;
import javax.annotation.Nullable;

public abstract class AbstractWsEvent extends AbstractRestEvent implements WsEvent {
    protected WsDirection direction;
    protected String endpointAddr;
    protected String payload;
    protected String prettyPayload;
    protected Map<String, Object> soapFault;
    protected Map<String, Object> soapHeaders;

    @JsonProperty
    @Override
    public WsDirection getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(WsDirection direction) {
        this.direction = direction;
    }

    @JsonProperty
    @Override
    public String getEndpointAddress() {
        return this.endpointAddr;
    }

    @Override
    public void setEndpointAddress(String endpointAddr) {
        this.endpointAddr = endpointAddr;
    }

    @JsonProperty
    @Override
    public String getPayload() {
        return this.payload;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean hasPrettyPayload() {
        return (this.prettyPayload != null);
    }

    @JsonProperty
    @Nullable
    @Override
    public String getPrettyPayload() {
        return this.prettyPayload;
    }

    @Override
    public void setPrettyPayload(@Nullable String prettyPayload) {
        this.prettyPayload = prettyPayload;
    }

    @Override
    public boolean hasSoapFault() {
        return (this.soapFault != null);
    }

    @JsonProperty
    @Nullable
    @Override
    public Map<String, Object> getSoapFault() {
        return this.soapFault;
    }

    @Override
    public void setSoapFault(@Nullable Map<String, Object> soapFault) {
        this.soapFault = soapFault;
    }

    @Override
    public boolean hasSoapHeaders() {
        return (this.soapHeaders != null);
    }

    @JsonProperty
    @Nullable
    @Override
    public Map<String, Object> getSoapHeaders() {
        return this.soapHeaders;
    }

    @Override
    public void setSoapHeaders(@Nullable Map<String, Object> soapHeaders) {
        this.soapHeaders = soapHeaders;
    }
}
