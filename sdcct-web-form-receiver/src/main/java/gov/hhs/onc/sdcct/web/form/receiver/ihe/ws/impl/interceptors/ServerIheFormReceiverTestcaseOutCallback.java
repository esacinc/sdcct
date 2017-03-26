package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutCallback;
import org.apache.cxf.message.Message;
import org.springframework.web.client.RestTemplate;

public class ServerIheFormReceiverTestcaseOutCallback
    extends AbstractServerIheTestcaseOutCallback<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult> {
    protected ServerIheFormReceiverTestcaseOutCallback(Message msg, Class<IheFormReceiverTestcaseResult> resultClass, String resultPropName,
        String eventEndpointAddr, RestTemplate restTemplate) {
        super(msg, resultClass, resultPropName, eventEndpointAddr, restTemplate);
    }
}
