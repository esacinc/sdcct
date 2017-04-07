package gov.hhs.onc.sdcct.testcases.ihe.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;

@JsonTypeName("iheFormArchiverTestcaseResult")
public class IheFormArchiverTestcaseResultImpl extends AbstractIheTestcaseResult<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission>
    implements IheFormArchiverTestcaseResult {
    public IheFormArchiverTestcaseResultImpl() {
        this(null);
    }

    public IheFormArchiverTestcaseResultImpl(IheFormArchiverTestcaseSubmission submission) {
        super(submission);
    }
}
