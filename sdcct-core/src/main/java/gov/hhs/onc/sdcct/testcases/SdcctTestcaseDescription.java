package gov.hhs.onc.sdcct.testcases;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.beans.DescriptionBean;
import java.util.List;
import javax.annotation.Nullable;

public interface SdcctTestcaseDescription extends DescriptionBean {
    public boolean hasInstructions();

    @JsonProperty
    @Nullable
    public String getInstructions();

    public void setInstructions(String instructions);

    public boolean hasSpecifications();

    @JsonProperty("specs")
    @Nullable
    public List<String> getSpecifications();

    public void setSpecifications(@Nullable List<String> specifications);

    public void setText(String text);
}
