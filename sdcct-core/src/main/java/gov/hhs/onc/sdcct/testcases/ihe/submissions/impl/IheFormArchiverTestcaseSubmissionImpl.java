package gov.hhs.onc.sdcct.testcases.ihe.submissions.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import javax.annotation.Nullable;

@JsonTypeName("iheFormArchiverTestcaseSubmission")
public class IheFormArchiverTestcaseSubmissionImpl extends AbstractIheTestcaseSubmission<IheFormArchiverTestcase> implements IheFormArchiverTestcaseSubmission {
    public IheFormArchiverTestcaseSubmissionImpl() {
        this(null, null, null);
    }

    public IheFormArchiverTestcaseSubmissionImpl(@Nullable IheFormArchiverTestcase testcase, @Nullable String endpointAddr, @Nullable String formId) {
        super(testcase, endpointAddr, formId);
    }
}
