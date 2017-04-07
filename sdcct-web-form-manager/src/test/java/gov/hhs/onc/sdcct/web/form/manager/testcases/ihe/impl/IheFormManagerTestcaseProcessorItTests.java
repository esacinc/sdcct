package gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.impl;

import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormManagerTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.web.test.impl.AbstractIheTestcaseProcessorItTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.form.manager.all", "sdcct.test.it.web.testcases.form.manager.ihe.all",
    "sdcct.test.it.web.testcases.form.manager.proc", "sdcct.test.it.web.testcases.form.manager.ihe.proc" })
public class IheFormManagerTestcaseProcessorItTests extends
    AbstractIheTestcaseProcessorItTests<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, RetrieveFormRequestType, IheFormManagerTestcaseProcessor> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private IheFormManagerTestcaseProcessor iheFormManagerTestcaseProc;

    @Value("${sdcct.ws.form.manager.rfd.url}")
    private String rfdFormManagerEndpointAddr;

    @Resource(name = "clientFormManagerRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClientFormManagerRfd;

    public IheFormManagerTestcaseProcessorItTests() {
        super(IheFormManagerTestcaseResultImpl.class, IheFormManagerTestcaseSubmissionImpl.class);
    }

    @Test
    public void testProcessIheFormManagerTestcasesSdcctInitiated() {
        super.testProcessIheTestcasesSdcctInitiated(this.iheFormManagerTestcaseProc, this.rfdFormManagerEndpointAddr);
    }

    @Test
    public void testProcessIheFormManagerTestcasesSutInitiated() {
        super.testProcessIheTestcasesSutInitiated(this.testClientFormManagerRfd);
    }

    @Override
    protected RetrieveFormRequestType createRequestInternal(IheFormManagerTestcase testcase) throws Exception {
        return testcase.getRequestParams();
    }

    @Override
    protected String findFormId(IheFormManagerTestcase testcase) {
        return testcase.getRequestParams().getWorkflowData().hasFormId() ? testcase.getRequestParams().getWorkflowData().getFormId() : null;
    }
}
