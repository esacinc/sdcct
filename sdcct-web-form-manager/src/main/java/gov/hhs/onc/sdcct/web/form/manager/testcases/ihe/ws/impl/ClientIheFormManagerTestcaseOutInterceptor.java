package gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractClientIheTestcaseOutInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormManagerTestcaseOut")
public class ClientIheFormManagerTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult> {
    public ClientIheFormManagerTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, IheFormManagerTestcaseResult.class);
    }
}
