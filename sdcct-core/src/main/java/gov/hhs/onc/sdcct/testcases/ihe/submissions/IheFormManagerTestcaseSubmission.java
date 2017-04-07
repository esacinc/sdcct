package gov.hhs.onc.sdcct.testcases.ihe.submissions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormManagerTestcaseSubmissionImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormManagerTestcaseSubmissionImpl.class) })
public interface IheFormManagerTestcaseSubmission extends IheTestcaseSubmission<IheFormManagerTestcase> {
}
