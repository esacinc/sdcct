package gov.hhs.onc.sdcct.testcases.ihe.submissions;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import javax.annotation.Nullable;
import org.hibernate.validator.constraints.NotEmpty;

public interface IheTestcaseSubmission<T extends IheTestcase> extends SdcctTestcaseSubmission<IheTestcaseDescription, T> {
    public boolean hasFormId();

    @JsonProperty
    @NotEmpty
    @Nullable
    public String getFormId();

    public void setFormId(@Nullable String formId);
}
