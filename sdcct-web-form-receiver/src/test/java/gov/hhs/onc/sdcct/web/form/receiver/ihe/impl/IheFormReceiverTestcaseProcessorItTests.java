package gov.hhs.onc.sdcct.web.form.receiver.ihe.impl;

import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.sdc.impl.SdcSubmissionPackageImpl;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormReceiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormReceiverTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.form.receiver.ihe.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.test.impl.AbstractIheTestcaseProcessorItTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.form.receiver.all", "sdcct.test.it.web.testcases.form.receiver.ihe.all",
    "sdcct.test.it.web.testcases.form.receiver.proc", "sdcct.test.it.web.testcases.form.receiver.ihe.proc" })
public class IheFormReceiverTestcaseProcessorItTests extends
    AbstractIheTestcaseProcessorItTests<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, AnyXmlContentType, IheFormReceiverTestcaseProcessor> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private IheFormReceiverTestcaseProcessor iheFormReceiverTestcaseProc;

    @Value("${sdcct.ws.form.receiver.rfd.url}")
    private String rfdFormReceiverEndpointAddr;

    @Resource(name = "clientFormReceiverRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClientFormReceiverRfd;

    public IheFormReceiverTestcaseProcessorItTests() {
        super(IheFormReceiverTestcaseResultImpl.class, IheFormReceiverTestcaseSubmissionImpl.class);
    }

    @Test
    public void testProcessIheFormReceiverTestcasesSdcctInitiated() {
        super.testProcessIheTestcasesSdcctInitiated(this.iheFormReceiverTestcaseProc, this.rfdFormReceiverEndpointAddr);
    }

    @Test
    public void testProcessIheFormReceiverTestcasesSutInitiated() {
        super.testProcessIheTestcasesSutInitiated(this.testClientFormReceiverRfd);
    }

    @Override
    protected AnyXmlContentType createRequestInternal(IheFormReceiverTestcase testcase) throws Exception {
        SdcSubmissionPackage submissionPackage = new SdcSubmissionPackageImpl();
        // noinspection ConstantConditions
        RfdForm form = testcase.hasForms() ? testcase.getForms().get(0) : null;

        if (form != null) {
            if (!form.hasBean()) {
                form = form.build();
            }

            submissionPackage.addFormDesigns(form.getBean());
        }

        return new AnyXmlContentTypeImpl().addAny(this.xmlCodec.encode(submissionPackage, new XdmDocumentDestination(this.config).getReceiver(), null)
            .getXdmNode().getDocument().getDocumentElement());
    }

    @Override
    protected String findFormId(IheFormReceiverTestcase testcase) {
        // noinspection ConstantConditions
        return testcase.hasForms() ? testcase.getForms().get(0).getIdentifier() : null;
    }
}
