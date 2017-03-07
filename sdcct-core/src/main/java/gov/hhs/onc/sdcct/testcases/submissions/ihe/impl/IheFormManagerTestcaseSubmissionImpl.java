package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import javax.annotation.Nullable;

@JsonTypeName("iheFormManagerTestcaseSubmission")
public class IheFormManagerTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormManagerTestcase> implements IheFormManagerTestcaseSubmission {
    public IheFormManagerTestcaseSubmissionImpl() {
        this(null, null, null);
    }

    public IheFormManagerTestcaseSubmissionImpl(@Nullable IheFormManagerTestcase testcase, @Nullable String endpointAddr, @Nullable String formId) {
        super(testcase, endpointAddr, formId);
    }
}
