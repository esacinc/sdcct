package gov.hhs.onc.sdcct.web.form.archiver.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;

public class ServerIheFormArchiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult> {
    protected ServerIheFormArchiverTestcaseOutCallback(IheTestcaseResultHandler resultHandler, Message msg, Class<IheFormArchiverTestcaseResult> resultClass,
        String resultPropName) {
        super(resultHandler, msg, resultClass, resultPropName);
    }
}
