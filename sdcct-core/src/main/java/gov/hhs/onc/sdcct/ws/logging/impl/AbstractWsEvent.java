package gov.hhs.onc.sdcct.ws.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.net.logging.RestEventType;
import gov.hhs.onc.sdcct.net.logging.impl.AbstractRestEvent;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils.SdcctToStringStyle;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.logging.WsEvent;
import gov.hhs.onc.sdcct.ws.logging.WsMessageType;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractWsEvent extends AbstractRestEvent implements WsEvent {
    protected String bindingName;
    protected WsDirection direction;
    protected String endpointAddr;
    protected String endpointName;
    protected WsMessageType msgType;
    protected String opName;
    protected String payload;
    protected String portName;
    protected String portTypeName;
    protected String prettyPayload;
    protected String resourceName;
    protected String serviceName;
    protected Map<String, Object> soapFault;
    protected Map<String, Object> soapHeaders;

    protected AbstractWsEvent(RestEventType eventType) {
        super(eventType);
    }

    @Override
    protected void buildMarkerMessages(StringBuffer msgBuffer, ToStringBuilder msgToStrBuilder, StringBuffer logstashFileMsgBuffer,
        ToStringBuilder logstashFileMsgToStrBuilder) {
        boolean msgTypeAvailable = this.hasMessageType(), soapMsgType = (this.msgType == WsMessageType.SOAP);
        String msgTypeId = (msgTypeAvailable ? this.msgType.getId() : null);

        if (msgTypeAvailable) {
            msgBuffer.append(msgTypeId);
            msgBuffer.append(" w");
        } else {
            msgBuffer.append("W");
        }

        msgBuffer.append("eb service ");
        msgBuffer.append(this.endpointType.getId());
        msgBuffer.append(StringUtils.SPACE);
        msgBuffer.append(this.eventType.getId());

        logstashFileMsgBuffer.append(msgBuffer);
        logstashFileMsgBuffer.append(SdcctStringUtils.PERIOD_CHAR);

        msgBuffer.append(" (");

        msgToStrBuilder.append("msgType", msgTypeId);
        msgToStrBuilder.append("endpointType", this.endpointType.getId());
        msgToStrBuilder.append("bindingName", this.bindingName);
        msgToStrBuilder.append("direction", this.direction.getId());
        msgToStrBuilder.append("endpointAddr", this.endpointAddr);
        msgToStrBuilder.append("endpointName", this.endpointName);

        if (soapMsgType) {
            msgToStrBuilder.append("portName", this.portName);
            msgToStrBuilder.append("portTypeName", this.portTypeName);
        }

        msgToStrBuilder.append("serviceName", this.serviceName);

        if (this.msgType == WsMessageType.REST) {
            msgToStrBuilder.append("resourceName", this.resourceName);
        } else if (soapMsgType) {
            msgToStrBuilder.append("soapHeaders", this.soapHeaders);
            msgToStrBuilder.append("soapFault", this.soapFault);
        }

        ((SdcctToStringStyle) msgToStrBuilder.getStyle()).removeLastFieldSeparator(msgBuffer);

        msgBuffer.append("):\n");
        msgBuffer.append((this.hasPrettyPayload() ? this.prettyPayload : this.payload));
    }

    @Override
    protected String buildMarkerFieldName() {
        return String.join(SdcctStringUtils.PERIOD, "ws", this.endpointType.getId(), this.eventType.getId());
    }

    @Override
    public boolean hasBindingName() {
        return (this.bindingName != null);
    }

    @Nullable
    @Override
    public String getBindingName() {
        return this.bindingName;
    }

    @Override
    public void setBindingName(@Nullable String bindingName) {
        this.bindingName = bindingName;
    }

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

    @Override
    public boolean hasEndpointName() {
        return (this.endpointName != null);
    }

    @Nullable
    @Override
    public String getEndpointName() {
        return this.endpointName;
    }

    @Override
    public void setEndpointName(@Nullable String endpointName) {
        this.endpointName = endpointName;
    }

    @Override
    public boolean hasMessageType() {
        return (this.msgType != null);
    }

    @Nullable
    @Override
    public WsMessageType getMessageType() {
        return this.msgType;
    }

    @Override
    public void setMessageType(@Nullable WsMessageType msgType) {
        this.msgType = msgType;
    }

    @Override
    public boolean hasOperationName() {
        return (this.opName != null);
    }

    @Nullable
    @Override
    public String getOperationName() {
        return this.opName;
    }

    @Override
    public void setOperationName(@Nullable String opName) {
        this.opName = opName;
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
    public boolean hasPortName() {
        return (this.portName != null);
    }

    @Nullable
    @Override
    public String getPortName() {
        return this.portName;
    }

    @Override
    public void setPortName(@Nullable String portName) {
        this.portName = portName;
    }

    @Override
    public boolean hasPortTypeName() {
        return (this.portTypeName != null);
    }

    @Nullable
    @Override
    public String getPortTypeName() {
        return this.portTypeName;
    }

    @Override
    public void setPortTypeName(@Nullable String portTypeName) {
        this.portTypeName = portTypeName;
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
    public boolean hasResourceName() {
        return (this.resourceName != null);
    }

    @Nullable
    @Override
    public String getResourceName() {
        return this.resourceName;
    }

    @Override
    public void setResourceName(@Nullable String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public boolean hasServiceName() {
        return (this.serviceName != null);
    }

    @Nullable
    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public void setServiceName(@Nullable String serviceName) {
        this.serviceName = serviceName;
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
