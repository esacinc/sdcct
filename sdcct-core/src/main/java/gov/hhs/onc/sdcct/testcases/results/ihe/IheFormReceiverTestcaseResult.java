package gov.hhs.onc.sdcct.testcases.results.ihe;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;

@JsonTypeName("iheFormReceiverTestcaseResult")
public interface IheFormReceiverTestcaseResult extends IheTestcaseResult<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission> {
}
