package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormArchiverTestcaseOut")
public class ServerIheFormArchiverTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, ServerIheFormArchiverTestcaseOutCallback> {
    public ServerIheFormArchiverTestcaseOutInterceptor() {
        super(IheFormArchiverTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormArchiverTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormArchiverTestcaseOutCallback(this.resultHandler, msg, this.resultClass, this.resultPropName);
    }
}
