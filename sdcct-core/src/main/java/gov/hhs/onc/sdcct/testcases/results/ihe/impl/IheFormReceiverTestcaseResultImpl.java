package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormReceiverTestcaseResultImpl.class) })
public class IheFormReceiverTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission>
    implements IheFormReceiverTestcaseResult {
    public IheFormReceiverTestcaseResultImpl(IheFormReceiverTestcaseSubmission submission) {
        super(submission);
    }
}
