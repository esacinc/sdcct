package gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractClientIheTestcaseOutInterceptor;
import org.springframework.stereotype.Component;

@Component("interceptorClientIheFormArchiverTestcaseOut")
public class ClientIheFormArchiverTestcaseOutInterceptor
    extends AbstractClientIheTestcaseOutInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult> {
    public ClientIheFormArchiverTestcaseOutInterceptor() {
        super(SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT, IheFormArchiverTestcaseResult.class);
    }
}
