package gov.hhs.onc.sdcct.testcases.submissions.ihe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormArchiverTestcaseSubmissionImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormArchiverTestcaseSubmissionImpl.class) })
public interface IheFormArchiverTestcaseSubmission extends IheTestcaseSubmission<IheFormArchiverTestcase> {
}
