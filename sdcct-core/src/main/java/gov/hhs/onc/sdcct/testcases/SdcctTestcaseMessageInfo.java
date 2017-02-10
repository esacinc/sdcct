package gov.hhs.onc.sdcct.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.annotation.Nullable;

public interface SdcctTestcaseMessageInfo {
    public boolean hasMessageName();

    @JsonProperty
    @Nullable
    public String getMessageName();

    public void setMessageName(@Nullable String messageName);

    public boolean hasMessageDetails();

    @JsonProperty
    @Nullable
    public Map<String, String> getMessageDetails();

    public void setMessageDetails(@Nullable Map<String, String> messageDetails);
}
