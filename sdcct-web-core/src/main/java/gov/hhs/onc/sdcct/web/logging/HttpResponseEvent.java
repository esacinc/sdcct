package gov.hhs.onc.sdcct.web.logging;

import gov.hhs.onc.sdcct.logging.MarkerFieldName;

@MarkerFieldName("httpResponse")
public interface HttpResponseEvent extends HttpEvent {
    public Integer getStatus();

    public void setStatus(Integer status);

    public String getStatusMessage();

    public void setStatusMessage(String statusMsg);
}
