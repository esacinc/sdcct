package gov.hhs.onc.sdcct.testcases.submissions.ihe;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;

public interface IheFormManagerTestcaseSubmission extends IheTestcaseSubmission<IheFormManagerTestcase> {
    public String getFormId();

    public void setFormId(String formId);
}
