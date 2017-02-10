package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;

public class IheFormArchiverTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission>
    implements IheFormArchiverTestcaseResult {
    public IheFormArchiverTestcaseResultImpl(IheFormArchiverTestcaseSubmission submission) {
        super(submission);
    }
}
