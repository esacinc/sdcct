package gov.hhs.onc.sdcct.web.logging.impl;

import gov.hhs.onc.sdcct.web.logging.HttpResponseEvent;

public class HttpResponseEventImpl extends AbstractHttpEvent implements HttpResponseEvent {
    private Integer status;
    private String statusMsg;

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String getStatusMessage() {
        return this.statusMsg;
    }

    @Override
    public void setStatusMessage(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
