package gov.hhs.onc.sdcct.web.form.receiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutInterceptor;
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
