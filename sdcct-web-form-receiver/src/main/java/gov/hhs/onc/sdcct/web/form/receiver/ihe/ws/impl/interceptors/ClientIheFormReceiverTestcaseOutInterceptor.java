package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseOutInterceptor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormReceiverTestcaseOut")
public class ClientIheFormReceiverTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult> {
    public ClientIheFormReceiverTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT, IheFormReceiverTestcaseResult.class);
    }
}
