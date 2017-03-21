package gov.hhs.onc.sdcct.web.form.archiver.ihe.ws.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors.AbstractServerIheTestcaseOutInterceptor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import javax.annotation.Resource;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("interceptorServerIheFormArchiverTestcaseOut")
public class ServerIheFormArchiverTestcaseOutInterceptor extends
    AbstractServerIheTestcaseOutInterceptor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult, ServerIheFormArchiverTestcaseOutCallback> {
    @Value("${sdcct.testcases.form.archiver.ihe.event.url}")
    private String iheFormArchiverTestcaseEventUrl;

    @Resource(name = "restTemplate")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected RestTemplate restTemplate;

    public ServerIheFormArchiverTestcaseOutInterceptor() {
        super(IheFormArchiverTestcaseResult.class, SdcctTestcasePropertyNames.IHE_FORM_ARCHIVER_TESTCASE_RESULT);
    }

    @Override
    protected ServerIheFormArchiverTestcaseOutCallback buildCallback(Message msg) {
        return new ServerIheFormArchiverTestcaseOutCallback(msg, this.resultClass, this.resultPropName, this.iheFormArchiverTestcaseEventUrl,
            this.restTemplate);
    }
}
