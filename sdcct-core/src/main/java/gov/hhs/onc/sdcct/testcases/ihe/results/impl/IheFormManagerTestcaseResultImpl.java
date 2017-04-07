package gov.hhs.onc.sdcct.testcases.ihe.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;

@JsonTypeName("iheFormManagerTestcaseResult")
public class IheFormManagerTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormManagerTestcase, IheFormManagerTestcaseSubmission>
    implements IheFormManagerTestcaseResult {
    public IheFormManagerTestcaseResultImpl() {
        this(null);
    }

    public IheFormManagerTestcaseResultImpl(IheFormManagerTestcaseSubmission submission) {
        super(submission);
    }
}
