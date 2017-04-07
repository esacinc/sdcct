package gov.hhs.onc.sdcct.testcases.ihe.submissions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormReceiverTestcaseSubmissionImpl;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({ @Type(IheFormReceiverTestcaseSubmissionImpl.class) })
public interface IheFormReceiverTestcaseSubmission extends IheTestcaseSubmission<IheFormReceiverTestcase> {
}
