package gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.impl;

import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.sdc.impl.SdcSubmissionPackageImpl;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormArchiverTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.web.test.impl.AbstractIheTestcaseProcessorItTests;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

@Test(groups = { "sdcct.test.it.web.testcases.form.archiver.all", "sdcct.test.it.web.testcases.form.archiver.ihe.all",
    "sdcct.test.it.web.testcases.form.archiver.proc", "sdcct.test.it.web.testcases.form.archiver.ihe.proc" })
public class IheFormArchiverTestcaseProcessorItTests extends
    AbstractIheTestcaseProcessorItTests<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, AnyXmlContentType, IheFormArchiverTestcaseProcessor> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private IheFormArchiverTestcaseProcessor iheFormArchiverTestcaseProcessor;

    @Value("${sdcct.ws.form.archiver.rfd.url}")
    private String rfdFormArchiverEndpointAddr;

    @Resource(name = "clientFormArchiverRfdLocal")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private JaxWsClient testClientFormArchiverRfd;

    public IheFormArchiverTestcaseProcessorItTests() {
        super(IheFormArchiverTestcaseResultImpl.class, IheFormArchiverTestcaseSubmissionImpl.class);
    }

    @Test
    public void testProcessIheFormArchiverTestcasesSdcctInitiated() {
        super.testProcessIheTestcasesSdcctInitiated(this.iheFormArchiverTestcaseProcessor, this.rfdFormArchiverEndpointAddr);
    }

    @Test
    public void testProcessIheFormArchiverTestcasesSutInitiated() {
        super.testProcessIheTestcasesSutInitiated(this.testClientFormArchiverRfd);
    }

    @Override
    protected AnyXmlContentType createRequestInternal(IheFormArchiverTestcase testcase) throws Exception {
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
    protected String findFormId(IheFormArchiverTestcase testcase) {
        // noinspection ConstantConditions
        return testcase.hasForms() ? testcase.getForms().get(0).getIdentifier() : null;
    }
}
