package gov.hhs.onc.sdcct.web.form.manager.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
import javax.annotation.Resource;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("interceptorServerIheFormManagerTestcaseOut")
public class ServerIheFormManagerTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult, ServerIheFormManagerTestcaseOutCallback> {
    @Value("${sdcct.testcases.form.manager.ihe.event.url}")
    private String iheFormManagerTestcaseEventUrl;

    @Resource(name = "restTemplate")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected RestTemplate restTemplate;

    public ServerIheFormManagerTestcaseOutInterceptor() {
        super(IheFormManagerTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormManagerTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormManagerTestcaseOutCallback(msg, this.resultClass, this.resultPropName, this.iheFormManagerTestcaseEventUrl, this.restTemplate);
    }
}
