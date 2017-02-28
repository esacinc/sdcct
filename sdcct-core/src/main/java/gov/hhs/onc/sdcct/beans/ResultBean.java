package gov.hhs.onc.sdcct.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ListMultimap;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import java.util.List;

public interface ResultBean {
    public boolean hasMessages(SdcctIssueSeverity severity);

    public boolean hasMessages();

    public List<String> getMessages(SdcctIssueSeverity severity);

    @JsonProperty("msgs")
    public ListMultimap<SdcctIssueSeverity, String> getMessages();

    @JsonProperty
    public boolean isSuccess();

    public void setSuccess(boolean success);
}
