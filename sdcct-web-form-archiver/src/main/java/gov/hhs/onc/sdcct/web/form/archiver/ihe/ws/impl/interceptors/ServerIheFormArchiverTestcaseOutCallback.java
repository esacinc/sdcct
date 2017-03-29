package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormArchiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult> {
    protected ServerIheFormArchiverTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormArchiverTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
