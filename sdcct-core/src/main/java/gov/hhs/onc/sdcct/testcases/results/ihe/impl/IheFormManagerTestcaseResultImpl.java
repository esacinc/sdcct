package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;

@JsonTypeName("iheFormManagerTestcaseResult")
public class IheFormManagerTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormManagerTestcase, IheFormManagerTestcaseSubmission>
    implements IheFormManagerTestcaseResult {
    public IheFormManagerTestcaseResultImpl(IheFormManagerTestcaseSubmission submission) {
        super(submission);
    }
}
