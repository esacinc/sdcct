package gov.hhs.onc.sdcct.testcases.steps;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.MessageBean;
import gov.hhs.onc.sdcct.beans.ResultBean;
import java.util.List;
import javax.annotation.Nullable;

public interface SdcctTestcaseStep extends ResultBean {
    @JsonProperty("msgs")
    @Override
    public List<MessageBean> getMessages();

    @Override
    public boolean isSuccess();

    public boolean hasDescription();

    @JsonProperty("desc")
    @Nullable
    public SdcctTestcaseStepDescription getDescription();

    public void setDescription(@Nullable SdcctTestcaseStepDescription desc);

    public boolean hasExecutionMessages();

    @JsonProperty
    public List<MessageBean> getExecutionMessages();

    public boolean isExecutionSuccess();

    public void setExecutionSuccess(boolean execSuccess);

    @JsonProperty("specType")
    public SpecificationType getSpecificationType();
}
