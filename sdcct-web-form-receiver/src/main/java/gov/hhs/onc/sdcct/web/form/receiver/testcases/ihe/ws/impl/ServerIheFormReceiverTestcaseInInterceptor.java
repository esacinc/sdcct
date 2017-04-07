package gov.hhs.onc.sdcct.web.form.receiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsaActions;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormReceiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormReceiverTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseInInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import org.apache.cxf.binding.soap.SoapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormReceiverTestcaseIn")
public class ServerIheFormReceiverTestcaseInInterceptor extends
    AbstractServerIheTestcaseInInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, AnyXmlContentType> {
    @Autowired
    private List<IheFormReceiverTestcase> iheFormReceiverTestcases;

    public ServerIheFormReceiverTestcaseInInterceptor() {
        super(AnyXmlContentTypeImpl.class, RfdWsXmlQnames.SUBMIT_FORM_REQ, SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT,
            IheFormReceiverTestcaseResult.class, IheFormReceiverTestcaseResultImpl.class, IheFormReceiverTestcaseSubmissionImpl.class,
            RfdWsaActions.SUBMIT_FORM);
    }

    @Override
    protected IheFormReceiverTestcaseResult processRequest(AnyXmlContentType request, SoapMessage message, @Nonnegative long submittedTimestamp, String txId)
        throws Exception {
        IheFormReceiverTestcaseResult result;

        if (request.getAny().get(0) instanceof SdcSubmissionPackage) {
            SdcSubmissionPackage submissionPackage = (SdcSubmissionPackage) request.getAny().get(0);
            List<FormDesignType> formDesigns = submissionPackage.getFormDesigns();

            if (!formDesigns.isEmpty()) {
                String formId = formDesigns.get(0).getId();
                IheFormReceiverTestcase iheFormReceiverTestcase = this.findTestcase(request, this.iheFormReceiverTestcases, formId);

                result = this.createResult(iheFormReceiverTestcase, message, formId, submittedTimestamp, txId);
            } else {
                result = this.createResult(null, message, null, submittedTimestamp, txId);
            }
        } else {
            result = this.createResult(null, message, null, submittedTimestamp, txId);
        }

        return result;
    }

    @Override
    protected IheFormReceiverTestcase findTestcase(AnyXmlContentType request, List<IheFormReceiverTestcase> testcases, String formId) {
        IheFormReceiverTestcase iheFormReceiverTestcase = null;

        for (IheFormReceiverTestcase testcase : this.iheFormReceiverTestcases) {
            if (!testcase.isSdcctInitiated()) {
                // noinspection ConstantConditions
                List<String> formIds =
                    testcase.hasForms() ? testcase.getForms().stream().map(RfdForm::getIdentifier).collect(Collectors.toList()) : new ArrayList<>();

                if (formIds.contains(formId)) {
                    iheFormReceiverTestcase = testcase;

                    break;
                }
            }
        }

        return iheFormReceiverTestcase;
    }

    @Override
    protected void validateRequest(IheFormReceiverTestcase testcase, IheFormReceiverTestcaseResult testcaseResult, AnyXmlContentType request) {
    }
}
