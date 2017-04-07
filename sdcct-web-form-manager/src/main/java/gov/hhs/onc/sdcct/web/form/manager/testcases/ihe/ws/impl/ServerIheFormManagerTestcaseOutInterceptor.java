package gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormManagerTestcaseOut")
public class ServerIheFormManagerTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, ServerIheFormManagerTestcaseOutCallback> {
    public ServerIheFormManagerTestcaseOutInterceptor() {
        super(IheFormManagerTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormManagerTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormManagerTestcaseOutCallback(this.resultHandler, msg, this.resultClass, this.resultPropName);
    }
}
