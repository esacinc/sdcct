package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormManagerTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.form.manager.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.test.impl.AbstractSdcctTestcaseProcessorItTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.form.manager.all", "sdcct.test.it.web.testcases.form.manager.ihe.all",
    "sdcct.test.it.web.testcases.form.manager.proc", "sdcct.test.it.web.testcases.form.manager.ihe.proc" })
public class IheFormManagerTestcaseProcessorItTests extends AbstractSdcctTestcaseProcessorItTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private IheFormManagerTestcaseProcessor iheFormManagerTestcaseProc;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<IheFormManagerTestcase> iheFormManagerTestcases;

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Resource(name = "clientFormManagerRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClientFormManagerRfd;

    @Test
    public void testProcessIheFormManagerTestcasesSdcctInitiated() {
        List<IheFormManagerTestcase> testcases =
            this.iheFormManagerTestcases.stream().filter(IheFormManagerTestcase::isSdcctInitiated).collect(Collectors.toList());

        for (IheFormManagerTestcase testcase : testcases) {
            IheFormManagerTestcaseSubmission submission = new IheFormManagerTestcaseSubmissionImpl(testcase, this.rfdFormManagerEndpointAddr);
            submission.setFormId(testcase.getRequestParams().getWorkflowData().getFormId());

            IheFormManagerTestcaseResult result = this.iheFormManagerTestcaseProc.process(submission);

            // TODO: verify result

            super.assertTestcaseProperties(testcase);
        }
    }

    @Test
    public void testProcessIheFormManagerTestcasesSutInitiated() {
        List<IheFormManagerTestcase> testcases =
            this.iheFormManagerTestcases.stream().filter(testcase -> !testcase.isSdcctInitiated()).collect(Collectors.toList());

        for (IheFormManagerTestcase testcase : testcases) {
            IheFormManagerTestcaseSubmission submission = new IheFormManagerTestcaseSubmissionImpl(testcase, this.rfdFormManagerEndpointAddr);
            submission.setFormId(testcase.getRequestParams().getWorkflowData().getFormId());

            Client delegate = this.testClientFormManagerRfd.buildInvocationDelegate();
            QName transaction = testcase.getTransaction();
            RetrieveFormRequestType retrieveFormRequestType = testcase.getRequestParams();
            String testcaseId = testcase.getId();

            try {
                RetrieveFormResponseType retrieveFormResponseType =
                    (RetrieveFormResponseType) this.testClientFormManagerRfd.invoke(delegate, transaction, retrieveFormRequestType)[0];

                Assert.assertNotNull(retrieveFormResponseType,
                    String.format("RetrieveFormResponseType for testcase (id=%s) was not expected to be null.", testcaseId));
            } catch (Exception e) {
                Assert.assertTrue(testcase.isNegative(), String.format("Testcase (id=%s) was expected to be negative.", testcaseId));
                Assert.assertTrue(e instanceof SoapFault, String.format("Testcase (id=%s) was expected to be a SoapFault", testcaseId));

                SoapFault soapFault = (SoapFault) e;

                Assert.assertNotNull(soapFault, String.format("SOAP fault for testcase (id=%s) was not expected to be null", testcaseId));
            }

            super.assertTestcaseProperties(testcase);
        }
    }

    private void assertTestcaseResultProperties(IheFormManagerTestcase testcase, IheFormManagerTestcaseResult result) {
        String testcaseId = testcase.getId();

        if (!testcase.isNegative()) {
            Assert.assertTrue(result.hasResponse(), String.format("Testcase (id=%s) was expected to have a response.", testcaseId));
            Assert.assertEquals(result.getMessages(SdcctIssueSeverity.ERROR).size(), 0,
                String.format("Result for testcase (id=%s) was not expected to have any error messages.", testcaseId));
            Assert.assertTrue(result.isSuccess(), String.format("Result for testcase (id=%s) was expected to be successful.", testcaseId));
        } else {
            Assert.assertTrue(result.hasFault(), String.format("Testcase (id=%s) was expected to have a SOAP fault.", testcaseId));
            Assert.assertTrue(result.getMessages(SdcctIssueSeverity.ERROR).size() > 0,
                String.format("Result for testcase (id=%s) was expected to have error messages.", testcaseId));
            Assert.assertFalse(result.isSuccess(), String.format("Result for testcase (id=%s) was not expected to be successful.", testcaseId));
        }
    }
}
