package gov.hhs.onc.sdcct.beans.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.ResultBean;
import java.util.List;

public abstract class AbstractResultBean implements ResultBean {
    protected ListMultimap<SdcctIssueSeverity, String> messages = ArrayListMultimap.create();
    protected boolean success;

    @Override
    public boolean hasMessages(SdcctIssueSeverity severity) {
        return this.messages.containsKey(severity);
    }

    @Override
    public boolean hasMessages() {
        return !this.messages.isEmpty();
    }

    @Override
    public List<String> getMessages(SdcctIssueSeverity severity) {
        return this.messages.get(severity);
    }

    @Override
    public ListMultimap<SdcctIssueSeverity, String> getMessages() {
        return this.messages;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
