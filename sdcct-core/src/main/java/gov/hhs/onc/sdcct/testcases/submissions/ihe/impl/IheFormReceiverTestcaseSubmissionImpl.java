package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import javax.annotation.Nullable;

@JsonTypeName("iheFormReceiverTestcaseSubmission")
public class IheFormReceiverTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormReceiverTestcase> implements IheFormReceiverTestcaseSubmission {
    public IheFormReceiverTestcaseSubmissionImpl() {
        this(null, null, null);
    }

    public IheFormReceiverTestcaseSubmissionImpl(@Nullable IheFormReceiverTestcase testcase, @Nullable String endpointAddr, @Nullable String formId) {
        super(testcase, endpointAddr, formId);
    }
}
