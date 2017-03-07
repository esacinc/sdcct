package gov.hhs.onc.sdcct.testcases.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;

@JsonTypeName("iheFormArchiverTestcase")
public class IheFormArchiverTestcaseImpl extends AbstractIheTestcase implements IheFormArchiverTestcase {
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
