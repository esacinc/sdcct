package gov.hhs.onc.sdcct.web.test.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcase;
import gov.hhs.onc.sdcct.testcases.SdcctTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.SdcctTestcaseSubmission;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.WsResponseEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.all", "sdcct.test.it.web.testcases.proc.all" })
public abstract class AbstractSdcctTestcaseProcessorItTests<T extends SdcctTestcaseDescription, U extends SdcctTestcase<T>, V extends SdcctTestcaseSubmission<T, U>, W extends SdcctTestcaseResult<T, U, V>>
    extends AbstractSdcctWebItTests implements BeanFactoryAware {
    protected BeanFactory beanFactory;

    protected Class<? extends W> resultImplClass;
    protected Class<? extends V> submissionImplClass;

    protected AbstractSdcctTestcaseProcessorItTests(Class<? extends W> resultImplClass, Class<? extends V> submissionImplClass) {
        this.resultImplClass = resultImplClass;
        this.submissionImplClass = submissionImplClass;
    }

    protected void assertTestcaseProperties(U testcase) {
        String testcaseId = testcase.getId();

        Assert.assertTrue(testcase.hasName(), String.format("Testcase (id=%s) was expected to have a name.", testcaseId));
        // noinspection ConstantConditions
        Assert.assertTrue(testcase.hasDescription() && testcase.getDescription().hasSpecifications(),
            String.format("Specifications for the testcase description for testcase (id=%s) were expected to be not null.", testcaseId));
        Assert.assertTrue(testcase.hasDescription() && testcase.getDescription().hasInstructions(),
            String.format("Instructions for the testcase description for testcase (id=%s) were expected to be not null.", testcaseId));
    }

    protected void assertTestcaseResultProperties(U testcase, W result) {
        String testcaseId = testcase.getId();

        Assert.assertNotNull(result, String.format("Testcase result for testcase (id=%s) was expected to be not null.", testcaseId));
        Assert.assertTrue(result.hasWsRequestEvent(), String.format("Testcase (id=%s) was expected to have a web service request event.", testcaseId));
        Assert.assertTrue(result.hasWsResponseEvent(), String.format("Testcase (id=%s) was expected to have a web service response event.", testcaseId));

        WsRequestEvent wsRequestEvent = result.getWsRequestEvent();
        WsResponseEvent wsResponseEvent = result.getWsResponseEvent();

        // noinspection ConstantConditions
        Assert.assertNotNull(wsRequestEvent.getPayload(),
            String.format("Web service request event payload for testcase (id=%s) was expected to be not null.", testcaseId));
        // noinspection ConstantConditions
        Assert.assertNotNull(wsResponseEvent.getPayload(),
            String.format("Web service response event payload for testcase (id=%s) was expected to be not null.", testcaseId));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
