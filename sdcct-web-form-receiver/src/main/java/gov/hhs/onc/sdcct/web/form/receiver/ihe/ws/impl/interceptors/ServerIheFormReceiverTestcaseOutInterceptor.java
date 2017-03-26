package gov.hhs.onc.sdcct.web.form.receiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
import javax.annotation.Resource;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("interceptorServerIheFormReceiverTestcaseOut")
public class ServerIheFormReceiverTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult, ServerIheFormReceiverTestcaseOutCallback> {
    @Value("${sdcct.testcases.form.receiver.ihe.event.url}")
    private String iheFormReceiverTestcaseEventUrl;

    @Resource(name = "restTemplate")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected RestTemplate restTemplate;

    public ServerIheFormReceiverTestcaseOutInterceptor() {
        super(IheFormReceiverTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_RECEIVER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormReceiverTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormReceiverTestcaseOutCallback(msg, this.resultClass, this.resultPropName, this.iheFormReceiverTestcaseEventUrl,
            this.restTemplate);
    }
}
