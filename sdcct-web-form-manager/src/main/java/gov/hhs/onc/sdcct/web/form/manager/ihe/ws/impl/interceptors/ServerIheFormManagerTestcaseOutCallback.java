package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormManagerTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult> {
    public ServerIheFormManagerTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormManagerTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
