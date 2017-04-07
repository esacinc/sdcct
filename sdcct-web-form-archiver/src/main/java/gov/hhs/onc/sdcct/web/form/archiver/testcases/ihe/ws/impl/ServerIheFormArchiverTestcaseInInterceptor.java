package gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsaActions;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.SdcSubmissionPackage;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormArchiverTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseInInterceptor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import org.apache.cxf.binding.soap.SoapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormArchiverTestcaseIn")
public class ServerIheFormArchiverTestcaseInInterceptor extends
    AbstractServerIheTestcaseInInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, AnyXmlContentType> {
    @Autowired
    private List<IheFormArchiverTestcase> iheFormArchiverTestcases;

    public ServerIheFormArchiverTestcaseInInterceptor() {
        super(AnyXmlContentTypeImpl.class, RfdWsXmlQnames.ARCHIVE_FORM_REQ, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT,
            IheFormArchiverTestcaseResult.class, IheFormArchiverTestcaseResultImpl.class, IheFormArchiverTestcaseSubmissionImpl.class,
            RfdWsaActions.ARCHIVE_FORM);
    }

    @Override
    protected IheFormArchiverTestcaseResult processRequest(AnyXmlContentType request, SoapMessage message, @Nonnegative long submittedTimestamp, String txId)
        throws Exception {
        IheFormArchiverTestcaseResult result;

        if (request.getAny().get(0) instanceof SdcSubmissionPackage) {
            SdcSubmissionPackage submissionPackage = (SdcSubmissionPackage) request.getAny().get(0);
            List<FormDesignType> formDesigns = submissionPackage.getFormDesigns();

            if (!formDesigns.isEmpty()) {
                String formId = formDesigns.get(0).getId();
                IheFormArchiverTestcase iheFormArchiverTestcase = this.findTestcase(request, this.iheFormArchiverTestcases, formId);

                result = this.createResult(iheFormArchiverTestcase, message, formId, submittedTimestamp, txId);
            } else {
                result = this.createResult(null, message, null, submittedTimestamp, txId);
            }
        } else {
            result = this.createResult(null, message, null, submittedTimestamp, txId);
        }

        return result;
    }

    @Override
    protected IheFormArchiverTestcase findTestcase(AnyXmlContentType request, List<IheFormArchiverTestcase> testcases, String formId) {
        IheFormArchiverTestcase iheFormArchiverTestcase = null;

        for (IheFormArchiverTestcase testcase : this.iheFormArchiverTestcases) {
            if (!testcase.isSdcctInitiated()) {
                // noinspection ConstantConditions
                List<String> formIds =
                    testcase.hasForms() ? testcase.getForms().stream().map(RfdForm::getIdentifier).collect(Collectors.toList()) : new ArrayList<>();

                if (formIds.contains(formId)) {
                    iheFormArchiverTestcase = testcase;

                    break;
                }
            }
        }

        return iheFormArchiverTestcase;
    }

    @Override
    protected void validateRequest(IheFormArchiverTestcase testcase, IheFormArchiverTestcaseResult testcaseResult, AnyXmlContentType request) {
    }
}
