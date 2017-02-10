package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import javax.annotation.Nullable;

public class IheFormManagerTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormManagerTestcase> implements IheFormManagerTestcaseSubmission {
    private String formId;

    public IheFormManagerTestcaseSubmissionImpl() {
        this(null, null);
    }

    public IheFormManagerTestcaseSubmissionImpl(@Nullable IheFormManagerTestcase testcase, @Nullable String endpointAddr) {
        super(testcase, endpointAddr);
    }

    @Override
    public String getFormId() {
        return this.formId;
    }

    @Override
    public void setFormId(String formId) {
        this.formId = formId;
    }
}
