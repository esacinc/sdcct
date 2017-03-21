package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseOutInterceptor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormManagerTestcaseOut")
public class ClientIheFormManagerTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult> {
    public ClientIheFormManagerTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, IheFormManagerTestcaseResult.class);
    }
}
