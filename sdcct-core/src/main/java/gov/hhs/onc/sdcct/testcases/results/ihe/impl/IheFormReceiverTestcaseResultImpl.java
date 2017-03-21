package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;

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
