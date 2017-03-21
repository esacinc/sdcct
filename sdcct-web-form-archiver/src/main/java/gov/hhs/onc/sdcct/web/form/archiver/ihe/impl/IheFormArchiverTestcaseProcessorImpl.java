package gov.hhs.onc.sdcct.web.form.archiver.ihe.impl;

import gov.hhs.onc.sdcct.api.RoleNames;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.sdc.impl.SdcSubmissionPackageImpl;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.archiver.ihe.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;

public class IheFormArchiverTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, AnyXmlContentType>
    implements IheFormArchiverTestcaseProcessor {
    protected IheFormArchiverTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT, RoleNames.FORM_ARCHIVER,
            SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_SUBMISSION, IheFormArchiverTestcaseResultImpl.class);
    }

    @Override
    protected AnyXmlContentType createRequestInternal(IheFormArchiverTestcaseSubmission submission) throws Exception {
        IheFormArchiverTestcase iheFormArchiverTestcase = submission.getTestcase();
        SdcSubmissionPackage submissionPackage = new SdcSubmissionPackageImpl();

        RfdForm form = null;

        // noinspection ConstantConditions
        if (iheFormArchiverTestcase.hasForms()) {
            // noinspection ConstantConditions
            form =
                iheFormArchiverTestcase.getForms().stream().filter(iheForm -> iheForm.getIdentifier().equals(submission.getFormId())).findFirst().orElse(null);
        }

        if (form != null) {
            if (!form.hasBean()) {
                form = form.build();
            }

            submissionPackage.addFormDesigns(form.getBean());
        }

        return new AnyXmlContentTypeImpl().addAny(this.xmlCodec.encode(submissionPackage, new XdmDocumentDestination(this.config).getReceiver(), null)
            .getXdmNode().getDocument().getDocumentElement());
    }
}
