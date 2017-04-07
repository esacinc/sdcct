package gov.hhs.onc.sdcct.web.form.receiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractClientIheTestcaseOutInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormReceiverTestcaseOut")
public class ClientIheFormReceiverTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult> {
    public ClientIheFormReceiverTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT, IheFormReceiverTestcaseResult.class);
    }
}
