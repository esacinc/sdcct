package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import javax.annotation.Nullable;

public class IheFormReceiverTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormReceiverTestcase> implements IheFormReceiverTestcaseSubmission {
    public IheFormReceiverTestcaseSubmissionImpl() {
        this(null, null);
    }

    public IheFormReceiverTestcaseSubmissionImpl(@Nullable IheFormReceiverTestcase testcase, @Nullable String endpointAddr) {
        super(testcase, endpointAddr);
    }
}
