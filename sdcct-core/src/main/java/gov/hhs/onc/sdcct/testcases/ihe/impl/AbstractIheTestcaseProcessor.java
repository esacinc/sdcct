package gov.hhs.onc.sdcct.testcases.ihe.impl;

import gov.hhs.onc.sdcct.testcases.impl.AbstractSdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;

public abstract class AbstractIheTestcaseProcessor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    extends AbstractSdcctTestcaseProcessor<IheTestcaseDescription, T, U, V> implements IheTestcaseProcessor<T, U, V> {
    protected AbstractIheTestcaseProcessor(String clientBeanName) {
        super(clientBeanName);
    }
}
