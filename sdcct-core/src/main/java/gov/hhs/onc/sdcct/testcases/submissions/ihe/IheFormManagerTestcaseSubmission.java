package gov.hhs.onc.sdcct.testcases.submissions.ihe;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormManagerTestcaseSubmissionImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormManagerTestcaseSubmissionImpl.class) })
public interface IheFormManagerTestcaseSubmission extends IheTestcaseSubmission<IheFormManagerTestcase> {
}
