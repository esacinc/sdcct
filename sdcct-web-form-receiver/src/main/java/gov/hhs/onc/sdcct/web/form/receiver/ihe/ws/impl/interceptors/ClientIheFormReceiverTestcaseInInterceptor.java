package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.SubmitFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.SubmitFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseInInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormReceiverTestcaseIn")
public class ClientIheFormReceiverTestcaseInInterceptor extends
    AbstractClientIheTestcaseInInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, SubmitFormResponseType> {
    public ClientIheFormReceiverTestcaseInInterceptor() {
        super(SubmitFormResponseType.class, SubmitFormResponseTypeImpl.class, IheFormReceiverTestcaseResult.class,
            SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT, SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_SUBMISSION,
            RfdWsXmlNames.SUBMIT_FORM_RESP);
    }

    @Override
    protected void validateResponseInternal(IheFormReceiverTestcase testcase, IheFormReceiverTestcaseSubmission submission,
        IheFormReceiverTestcaseResult result, SubmitFormResponseType actualResponse) {
        SubmitFormResponseType expectedResponse = testcase.getResponse();
        // noinspection ConstantConditions
        String expectedContentType = expectedResponse.getContentType();

        if (!actualResponse.getContentType().equals(expectedContentType)) {
            result.getMessages().get(SdcctIssueSeverity.ERROR).add(String.format("%s contains unexpected contentType (expected=%s, actual=%s).",
                this.wsResponseName, expectedContentType, actualResponse.getContentType()));
        }
    }
}
