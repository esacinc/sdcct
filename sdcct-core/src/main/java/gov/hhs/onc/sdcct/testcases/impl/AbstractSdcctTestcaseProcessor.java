package gov.hhs.onc.sdcct.testcases.impl;

import gov.hhs.onc.sdcct.logging.impl.TxIdGenerator;
import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public abstract class AbstractSdcctTestcaseProcessor<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
    implements SdcctTestcaseProcessor<T, U, V, W> {
    @Resource(name = "txIdGenTestcaseProcess")
    protected TxIdGenerator txIdGen;

    protected BeanFactory beanFactory;
    protected String clientBeanName;

    protected AbstractSdcctTestcaseProcessor(String clientBeanName) {
        this.clientBeanName = clientBeanName;
    }

    @Override
    public W process(V submission) {
        long submittedTimestamp = System.currentTimeMillis();

        submission.setSubmittedTimestamp(submittedTimestamp);
        submission.setTxId(this.txIdGen.generateId(submittedTimestamp).toString());

        W result = null;

        try {
            result = this.processInternal(submission);
        } finally {
            if (result != null) {
                result.setProcessedTimestamp(System.currentTimeMillis());
            }
        }

        return result;
    }

    protected abstract W processInternal(V submission);

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
