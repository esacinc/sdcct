package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormReceiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult> {
    protected ServerIheFormReceiverTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormReceiverTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
