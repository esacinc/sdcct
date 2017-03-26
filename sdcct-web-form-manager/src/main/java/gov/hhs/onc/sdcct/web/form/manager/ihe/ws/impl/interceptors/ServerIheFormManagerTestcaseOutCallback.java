package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;
import org.springframework.web.client.RestTemplate;

public class ServerIheFormManagerTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult> {
    public ServerIheFormManagerTestcaseOutCallback(Message msg, Class<IheFormManagerTestcaseResult> resultClass, String resultPropName,
        String eventEndpointAddr, RestTemplate restTemplate) {
        super(msg, resultClass, resultPropName, eventEndpointAddr, restTemplate);
    }
}
