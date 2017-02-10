package gov.hhs.onc.sdcct.testcases;

import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import org.springframework.beans.factory.BeanFactoryAware;

public interface SdcctTestcaseProcessor<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
    extends BeanFactoryAware {
    public W process(V submission);
}
