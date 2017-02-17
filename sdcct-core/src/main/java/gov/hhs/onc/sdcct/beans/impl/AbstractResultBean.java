package gov.hhs.onc.sdcct.beans.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.MessageBean;
import gov.hhs.onc.sdcct.beans.ResultBean;
import java.util.List;

public abstract class AbstractResultBean implements ResultBean {
    protected ListMultimap<SdcctIssueSeverity, MessageBean> messages = ArrayListMultimap.create();

    @Override
    public boolean hasMessages(SdcctIssueSeverity severity) {
        return this.messages.containsKey(severity);
    }

    @Override
    public boolean hasMessages() {
        return !this.getMessages().isEmpty();
    }

    @Override
    public List<MessageBean> getMessages(SdcctIssueSeverity severity) {
        return this.getMessages().get(severity);
    }

    @Override
    public ListMultimap<SdcctIssueSeverity, MessageBean> getMessages() {
        return this.messages;
    }

    @Override
    public boolean isSuccess() {
        return !this.hasMessages(SdcctIssueSeverity.ERROR);
    }
}
