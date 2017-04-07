package gov.hhs.onc.sdcct.web.form.receiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormReceiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult> {
    protected ServerIheFormReceiverTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormReceiverTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
