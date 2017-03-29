package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
import org.apache.cxf.message.Message;
import org.springframework.stereotype.Component;

@Component("interceptorServerIheFormReceiverTestcaseOut")
public class ServerIheFormReceiverTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, ServerIheFormReceiverTestcaseOutCallback> {
    public ServerIheFormReceiverTestcaseOutInterceptor() {
        super(IheFormReceiverTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormReceiverTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormReceiverTestcaseOutCallback(this.resultHandler, msg, this.resultClass, this.resultPropName);
    }
}
