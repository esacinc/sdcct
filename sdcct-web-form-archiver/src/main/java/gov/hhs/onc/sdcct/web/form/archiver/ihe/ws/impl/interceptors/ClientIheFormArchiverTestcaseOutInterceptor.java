package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractClientIheTestcaseOutInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormArchiverTestcaseOut")
public class ClientIheFormArchiverTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult> {
    public ClientIheFormArchiverTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT, IheFormArchiverTestcaseResult.class);
    }
}
