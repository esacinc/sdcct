package gov.hhs.onc.sdcct.testcases.ihe;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.testcases.ihe.impl.IheFormReceiverTestcaseImpl;

@JsonSubTypes({ @Type(IheFormReceiverTestcaseImpl.class) })
public interface IheFormReceiverTestcase extends IheTestcase {
    @JsonProperty
    public String getFormId();

    public void setFormId(String formId);
}
