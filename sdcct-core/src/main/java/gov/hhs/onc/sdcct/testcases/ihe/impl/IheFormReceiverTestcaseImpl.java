package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;

@JsonTypeName("iheFormReceiverTestcase")
public class IheFormReceiverTestcaseImpl extends AbstractIheTestcase implements IheFormReceiverTestcase {
    private String formId;

    @Override
    public String getFormId() {
        return this.formId;
    }

    @Override
    public void setFormId(String formId) {
        this.formId = formId;
    }
}
