package gov.hhs.onc.sdcct.net.http.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.net.http.logging.impl.HttpResponseEventImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(HttpResponseEventImpl.class) })
public interface HttpResponseEvent extends HttpEvent {
    @JsonProperty
    public Integer getStatusCode();

    public void setStatusCode(Integer statusCode);

    @JsonProperty
    public String getStatusMessage();

    public void setStatusMessage(String statusMsg);
}
