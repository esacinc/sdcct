package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.MessageBean;

public class MessageBeanImpl implements MessageBean {
    private SdcctIssueSeverity severity;
    private String text;

    public MessageBeanImpl(SdcctIssueSeverity severity, String text) {
        this.severity = severity;
        this.text = text;
    }

    public SdcctIssueSeverity getSeverity() {
        return this.severity;
    }

    @Override
    public String getText() {
        return this.text;
    }
}
