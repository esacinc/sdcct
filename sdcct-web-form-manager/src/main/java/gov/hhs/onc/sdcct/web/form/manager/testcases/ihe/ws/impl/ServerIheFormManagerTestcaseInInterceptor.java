package gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.EncodedResponse;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormRequestTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsaActions;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.impl.IheFormManagerTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseInInterceptor;
import java.util.List;
import javax.annotation.Nonnegative;
import org.apache.cxf.binding.soap.SoapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormManagerTestcaseIn")
public class ServerIheFormManagerTestcaseInInterceptor extends
    AbstractServerIheTestcaseInInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, RetrieveFormRequestType> {
    @Autowired
    private List<IheFormManagerTestcase> iheFormManagerTestcases;

    public ServerIheFormManagerTestcaseInInterceptor() {
        super(RetrieveFormRequestTypeImpl.class, RfdWsXmlQnames.RETRIEVE_FORM_REQ, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT,
            IheFormManagerTestcaseResult.class, IheFormManagerTestcaseResultImpl.class, IheFormManagerTestcaseSubmissionImpl.class,
            RfdWsaActions.RETRIEVE_FORM);
    }

    @Override
    protected IheFormManagerTestcaseResult processRequest(RetrieveFormRequestType request, SoapMessage message, @Nonnegative long submittedTimestamp,
        String txId) throws Exception {
        String formId = request.getWorkflowData().getFormId();
        IheFormManagerTestcase iheFormManagerTestcase = this.findTestcase(request, this.iheFormManagerTestcases, formId);

        return this.createResult(iheFormManagerTestcase, message, formId, submittedTimestamp, txId);
    }

    @Override
    protected IheFormManagerTestcase findTestcase(RetrieveFormRequestType request, List<IheFormManagerTestcase> testcases, String formId) {
        IheFormManagerTestcase iheFormManagerTestcase = null;

        EncodedResponse actualEncodedResponse = request.getWorkflowData().getEncodedResponse();

        for (IheFormManagerTestcase testcase : this.iheFormManagerTestcases) {
            if (!testcase.isSdcctInitiated()) {
                EncodedResponse expectedEncodedResponse = testcase.getRequestParams().getWorkflowData().getEncodedResponse();

                if (expectedEncodedResponse.getResponseContentType().equals(actualEncodedResponse.getResponseContentType()) &&
                    expectedEncodedResponse.getValue() == actualEncodedResponse.getValue()) {
                    iheFormManagerTestcase = testcase;

                    break;
                }
            }
        }

        return iheFormManagerTestcase;
    }

    @Override
    protected void validateRequest(IheFormManagerTestcase testcase, IheFormManagerTestcaseResult testcaseResult, RetrieveFormRequestType actualRequest) {
        RetrieveFormRequestType expectedRequest = testcase.getRequestParams();

        EncodedResponse actualEncodedResponse = actualRequest.getWorkflowData().getEncodedResponse(),
            expectedEncodedResponse = expectedRequest.getWorkflowData().getEncodedResponse();

        if (expectedEncodedResponse.getValue() != actualEncodedResponse.getValue()) {
            testcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("%s for testcase (id=%s) does not contain the expected encodedResponse value (expected=%s, actual=%s).", this.requestQName,
                    testcase.getId(), expectedEncodedResponse.getValue(), actualEncodedResponse.getValue()));
        }

        if (!expectedEncodedResponse.getResponseContentType().equals(actualEncodedResponse.getResponseContentType())) {
            testcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("%s for testcase (id=%s) does not contain the expected responseContentType value (expected=%s, actual=%s).",
                    this.requestQName, testcase.getId(), expectedEncodedResponse.getResponseContentType(), actualEncodedResponse.getResponseContentType()));
        }
    }
}
