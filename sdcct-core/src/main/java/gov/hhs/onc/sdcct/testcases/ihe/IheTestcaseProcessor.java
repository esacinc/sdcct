package gov.hhs.onc.sdcct.testcases.ihe;

import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;

public interface IheTestcaseProcessor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    extends SdcctTestcaseProcessor<IheTestcaseDescription, T, U, V> {
}
