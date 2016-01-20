package gov.hhs.onc.sdcct.net.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.logging.MarkerFieldName;

@MarkerFieldName("httpResponse")
public interface HttpResponseEvent extends HttpEvent {
    @JsonProperty
    public Integer getStatusCode();

    public void setStatusCode(Integer statusCode);

    @JsonProperty
    public String getStatusMessage();

    public void setStatusMessage(String statusMsg);
}
