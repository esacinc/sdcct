package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import org.apache.cxf.message.Message;
import org.springframework.web.client.RestTemplate;

public class ServerIheFormArchiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult> {
    protected ServerIheFormArchiverTestcaseOutCallback(Message msg, Class<IheFormArchiverTestcaseResult> resultClass, String resultPropName,
        String eventEndpointAddr, RestTemplate restTemplate) {
        super(msg, resultClass, resultPropName, eventEndpointAddr, restTemplate);
    }
}
