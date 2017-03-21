package gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
import java.io.OutputStream;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractServerIheTestcaseOutCallback<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    extends AbstractIheTestcaseOutCallback<T, U, V> {
    protected String eventEndpointAddr;
    protected RestTemplate restTemplate;

    protected AbstractServerIheTestcaseOutCallback(Message msg, Class<V> resultClass, String resultPropName, String eventEndpointAddr,
        RestTemplate restTemplate) {
        super(msg, resultClass, resultPropName);

        this.eventEndpointAddr = eventEndpointAddr;
        this.restTemplate = restTemplate;
    }

    protected void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception {
        try {
            Message inMsg = this.msg.getExchange().getInMessage();
            Object resultPropValue = inMsg.getContextualProperty(this.resultPropName);

            if (resultPropValue != null) {
                V result = SdcctTestcaseUtils.addWsResponseEvent(inMsg, this.resultPropName, this.resultClass, this.msg);
                U submission = result.getSubmission();
                T testcase = submission.getTestcase();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                Fault fault = ((Fault) this.msg.getContent(Exception.class));
                result.setFault(fault);

                if (result.hasFault()) {
                    if (testcase != null && testcase.isNegative()) {
                        result.getMessages(SdcctIssueSeverity.INFORMATION)
                            .add(String.format(
                                "Please check that the web service response event payload contains a SOAP fault (message=%s) that corresponds to the associated testcase (id=%s) description.",
                                fault.getMessage(), testcase != null ? testcase.getId() : "None"));
                    } else {
                        result.getMessages(SdcctIssueSeverity.INFORMATION)
                            .add(String.format(
                                "Please check that the web service response event payload corresponds to what is expected in the associated testcase (id=%s) description.",
                                fault.getMessage(), testcase != null ? testcase.getId() : "None"));
                    }
                }

                // noinspection ConstantConditions
                if (!result.hasMessages(SdcctIssueSeverity.ERROR) && submission.hasTestcase() && (!testcase.isNegative() && fault == null)) {
                    result.setSuccess(true);
                }

                this.sendResult(new HttpEntity<>(result, headers));
            }
        } finally {
            OutputStream outStream = cachedStream.getFlowThroughStream();

            cachedStream.lockOutputStream();
            cachedStream.resetOut(null, false);

            this.msg.setContent(OutputStream.class, outStream);
        }
    }

    @Override
    protected void sendResult(HttpEntity<V> httpEntity) {
        this.restTemplate.postForEntity(this.eventEndpointAddr, httpEntity, this.resultClass);
    }
}
