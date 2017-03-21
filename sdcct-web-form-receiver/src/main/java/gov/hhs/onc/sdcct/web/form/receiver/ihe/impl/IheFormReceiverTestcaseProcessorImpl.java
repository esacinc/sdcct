package gov.hhs.onc.sdcct.web.form.receiver.ihe.impl;

import gov.hhs.onc.sdcct.api.RoleNames;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.sdc.impl.SdcSubmissionPackageImpl;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormReceiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.receiver.ihe.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;

public class IheFormReceiverTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, AnyXmlContentType>
    implements IheFormReceiverTestcaseProcessor {
    public IheFormReceiverTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName, SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT, RoleNames.FORM_RECEIVER,
            SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_SUBMISSION, IheFormReceiverTestcaseResultImpl.class);
    }

    @Override
    protected AnyXmlContentType createRequestInternal(IheFormReceiverTestcaseSubmission submission) throws Exception {
        IheFormReceiverTestcase iheFormReceiverTestcase = submission.getTestcase();
        SdcSubmissionPackage submissionPackage = new SdcSubmissionPackageImpl();

        RfdForm form = null;

        // noinspection ConstantConditions
        if (iheFormReceiverTestcase.hasForms()) {
            // noinspection ConstantConditions
            form =
                iheFormReceiverTestcase.getForms().stream().filter(iheForm -> iheForm.getIdentifier().equals(submission.getFormId())).findFirst().orElse(null);
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
