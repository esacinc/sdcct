package gov.hhs.onc.sdcct.testcases.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract class AbstractSdcctTestcaseProcessor<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
    implements SdcctTestcaseProcessor<T, U, V, W> {
    protected BeanFactory beanFactory;
    protected String clientBeanName;

    protected AbstractSdcctTestcaseProcessor(String clientBeanName) {
        this.clientBeanName = clientBeanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
