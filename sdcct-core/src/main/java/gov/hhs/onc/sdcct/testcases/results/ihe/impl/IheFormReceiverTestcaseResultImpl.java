package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;

public class IheFormReceiverTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission> {
    public IheFormReceiverTestcaseResultImpl(IheFormReceiverTestcaseSubmission submission) {
        super(submission);
    }
}
