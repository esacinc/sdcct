package gov.hhs.onc.sdcct.web.test.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.all", "sdcct.test.it.web.testcases.proc.all", "sdcct.test.it.web.testcases.ihe.all",
    "sdcct.test.it.web.testcases.ihe.proc.all" })
public abstract class AbstractIheTestcaseProcessorItTests<U extends IheTestcase, V extends IheTestcaseSubmission<U>, W extends IheTestcaseResult<U, V>, X, Y extends IheTestcaseProcessor<U, V, W>>
    extends AbstractSdcctTestcaseProcessorItTests<IheTestcaseDescription, U, V, W> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected SdcctSaxonConfiguration config;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected XmlCodec xmlCodec;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected List<U> testcases;

    protected AbstractIheTestcaseProcessorItTests(Class<? extends W> resultImplClass, Class<? extends V> submissionImplClass) {
        super(resultImplClass, submissionImplClass);
    }

    protected void testProcessIheTestcasesSdcctInitiated(Y testcaseProcessor, String endpointAddr) {
        List<U> filteredTestcases = this.testcases.stream().filter(U::isSdcctInitiated).collect(Collectors.toList());

        for (U testcase : filteredTestcases) {
            V submission = this.beanFactory.getBean(this.submissionImplClass, testcase, endpointAddr, this.findFormId(testcase));

            super.assertTestcaseProperties(testcase);
            assertTestcaseResultProperties(testcase, testcaseProcessor.process(submission));
        }
    }

    protected void testProcessIheTestcasesSutInitiated(JaxWsClient rfdClient) {
        List<U> filteredTestcases = this.testcases.stream().filter(testcase -> !testcase.isSdcctInitiated()).collect(Collectors.toList());

        for (U testcase : filteredTestcases) {
            Client delegate = rfdClient.buildInvocationDelegate();
            QName transaction = testcase.getTransaction();
            String testcaseId = testcase.getId();

            try {
                Assert.assertNotNull(rfdClient.invoke(delegate, transaction, this.createRequest(testcase))[0],
                    String.format("Response for testcase (id=%s) was not expected to be null.", testcaseId));
            } catch (Exception e) {
                Assert.assertTrue(testcase.isNegative(), String.format("Testcase (id=%s) was expected to be negative.", testcaseId));
                Assert.assertTrue(e instanceof SoapFault, String.format("Testcase (id=%s) was expected to be a SoapFault", testcaseId));

                SoapFault soapFault = (SoapFault) e;

                Assert.assertNotNull(soapFault, String.format("SOAP fault for testcase (id=%s) was not expected to be null", testcaseId));
            }

            super.assertTestcaseProperties(testcase);
        }
    }

    protected void assertTestcaseResultProperties(U testcase, W result) {
        super.assertTestcaseResultProperties(testcase, result);

        String testcaseId = testcase.getId();

        if (!testcase.isNegative()) {
            Assert.assertTrue(result.hasResponse(), String.format("Testcase (id=%s) was expected to have a response.", testcaseId));
            Assert.assertEquals(result.getMessages(SdcctIssueSeverity.ERROR).size(), 0,
                String.format("Result for testcase (id=%s) was not expected to have any error messages.", testcaseId));
            Assert.assertTrue(result.isSuccess(), String.format("Result for testcase (id=%s) was expected to be successful.", testcaseId));
        } else {
            Assert.assertTrue(result.hasFault(), String.format("Testcase (id=%s) was expected to have a SOAP fault.", testcaseId));
            Assert.assertTrue(result.getMessages(SdcctIssueSeverity.INFORMATION).size() > 0,
                String.format("Result for testcase (id=%s) was expected to have informational messages.", testcaseId));
            Assert.assertFalse(result.isSuccess(), String.format("Result for testcase (id=%s) was not expected to be successful.", testcaseId));
        }
    }

    protected X createRequest(U testcase) throws Exception {
        return this.createRequestInternal(testcase);
    }

    protected abstract X createRequestInternal(U testcase) throws Exception;

    protected abstract String findFormId(U testcase);
}
