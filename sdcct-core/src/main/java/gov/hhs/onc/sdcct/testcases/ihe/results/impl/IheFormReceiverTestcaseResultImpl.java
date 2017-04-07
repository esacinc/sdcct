package gov.hhs.onc.sdcct.testcases.ihe.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;

@JsonTypeName("iheFormReceiverTestcaseResult")
public class IheFormReceiverTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission>
    implements IheFormReceiverTestcaseResult {
    public IheFormReceiverTestcaseResultImpl() {
        this(null);
    }

    public IheFormReceiverTestcaseResultImpl(IheFormReceiverTestcaseSubmission submission) {
        super(submission);
    }
}
