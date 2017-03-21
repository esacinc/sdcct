package gov.hhs.onc.sdcct.testcases.results.ihe.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;

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
