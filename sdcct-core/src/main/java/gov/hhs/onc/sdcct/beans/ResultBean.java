package gov.hhs.onc.sdcct.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import java.util.List;

public interface ResultBean {
    public boolean hasMessages(SdcctIssueSeverity severity);

    public boolean hasMessages();

    public List<MessageBean> getMessages(SdcctIssueSeverity severity);

    @JsonProperty("msgs")
    public ListMultimap<SdcctIssueSeverity, MessageBean> getMessages();

    @JsonProperty
    public boolean isSuccess();

    public void setSuccess(boolean success);
}
