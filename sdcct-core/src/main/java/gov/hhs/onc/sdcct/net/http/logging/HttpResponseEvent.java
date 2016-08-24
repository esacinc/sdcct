package gov.hhs.onc.sdcct.net.http.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface HttpResponseEvent extends HttpEvent {
    @JsonProperty
    public Integer getStatusCode();

    public void setStatusCode(Integer statusCode);

    @JsonProperty
    public String getStatusMessage();

    public void setStatusMessage(String statusMsg);
}
