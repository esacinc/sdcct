package gov.hhs.onc.sdcct.testcases.submissions.ihe.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import javax.annotation.Nullable;

public class IheFormArchiverTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormArchiverTestcase> implements IheFormArchiverTestcaseSubmission {
    public IheFormArchiverTestcaseSubmissionImpl() {
        this(null, null);
    }

    public IheFormArchiverTestcaseSubmissionImpl(@Nullable IheFormArchiverTestcase testcase, @Nullable String endpointAddr) {
        super(testcase, endpointAddr);
    }
}
