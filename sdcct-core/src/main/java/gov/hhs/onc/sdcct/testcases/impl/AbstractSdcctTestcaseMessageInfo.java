package gov.hhs.onc.sdcct.testcases.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcaseMessageInfo;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSdcctTestcaseMessageInfo implements SdcctTestcaseMessageInfo {
    protected String messageName;
    protected Map<String, String> messageDetails;

    @Override
    public boolean hasMessageName() {
        return !StringUtils.isBlank(this.messageName);
    }

    @Override
    public String getMessageName() {
        return this.messageName;
    }

    @Override
    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    @Override
    public boolean hasMessageDetails() {
        return !this.messageDetails.isEmpty();
    }

    @Override
    public Map<String, String> getMessageDetails() {
        return this.messageDetails;
    }

    @Override
    public void setMessageDetails(Map<String, String> messageDetails) {
        this.messageDetails = messageDetails;
    }
}
