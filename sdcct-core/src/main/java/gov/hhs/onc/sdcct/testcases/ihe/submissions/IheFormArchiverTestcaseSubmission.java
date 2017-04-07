package gov.hhs.onc.sdcct.testcases.ihe.submissions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormArchiverTestcaseSubmissionImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormArchiverTestcaseSubmissionImpl.class) })
public interface IheFormArchiverTestcaseSubmission extends IheTestcaseSubmission<IheFormArchiverTestcase> {
}
