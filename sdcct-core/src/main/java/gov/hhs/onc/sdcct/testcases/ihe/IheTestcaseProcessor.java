package gov.hhs.onc.sdcct.testcases.ihe;

import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;

public interface IheTestcaseProcessor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    extends SdcctTestcaseProcessor<IheTestcaseDescription, T, U, V> {
}
