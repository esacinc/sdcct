package gov.hhs.onc.sdcct.web.form.manager.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormManagerTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult> {
    public ServerIheFormManagerTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormManagerTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
