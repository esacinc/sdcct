package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
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
