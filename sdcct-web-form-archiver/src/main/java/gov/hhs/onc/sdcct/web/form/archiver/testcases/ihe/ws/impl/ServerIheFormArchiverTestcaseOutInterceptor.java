package gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutInterceptor;
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
