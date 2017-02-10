package gov.hhs.onc.sdcct.testcases.submissions.ihe;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;

public interface IheTestcaseSubmission<T extends IheTestcase> extends SdcctTestcaseSubmission<IheTestcaseDescription, T> {
}
