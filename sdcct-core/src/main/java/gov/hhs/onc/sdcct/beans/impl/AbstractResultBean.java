package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.MessageBean;
import gov.hhs.onc.sdcct.beans.ResultBean;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractResultBean implements ResultBean {
    protected List<MessageBean> messages = new ArrayList<>();

    @Override
    public boolean hasMessages(SdcctIssueSeverity severity) {
        return (this.hasMessages() && this.getMessages().stream().anyMatch(msg -> (msg.getSeverity() == severity)));
    }

    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }

    @Override
    public List<MessageBean> getMessages(SdcctIssueSeverity severity) {
        return this.getMessages().stream().filter(msg -> (msg.getSeverity() == severity)).collect(Collectors.toList());
    }

    @Override
    public List<MessageBean> getMessages() {
        return this.messages;
    }

    @Override
    public boolean isSuccess() {
        return !this.hasMessages(SdcctIssueSeverity.ERROR);
    }
}
