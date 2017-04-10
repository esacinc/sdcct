package gov.hhs.onc.sdcct.testcases.ihe.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseDescription;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.impl.AbstractSdcctTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.transform.saxon.impl.SdcctSaxonConfiguration;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.endpoint.Client;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractIheTestcaseProcessor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>, W>
    extends AbstractSdcctTestcaseProcessor<IheTestcaseDescription, T, U, V> implements IheTestcaseProcessor<T, U, V> {
    @Autowired
    protected SdcctSaxonConfiguration config;

    @Autowired
    protected XmlCodec xmlCodec;

    protected String resultPropName;
    protected String roleName;
    protected String submissionPropName;
    protected Class<? extends V> resultImplClass;

    protected AbstractIheTestcaseProcessor(String clientBeanName, String resultPropName, String roleName, String submissionPropName,
        Class<? extends V> resultImplClass) {
        super(clientBeanName);

        this.resultPropName = resultPropName;
        this.roleName = roleName;
        this.submissionPropName = submissionPropName;
        this.resultImplClass = resultImplClass;
    }

    @Override
    protected V processInternal(U submission) {
        T testcase = submission.getTestcase();
        String endpointAddr = submission.getEndpointAddress();
        JaxWsClient client = null;

        try {
            Client delegate = (client = ((JaxWsClient) this.beanFactory.getBean(this.clientBeanName, endpointAddr))).buildInvocationDelegate();
            BindingProvider bindingProvider = ((BindingProvider) delegate);

            V result = this.beanFactory.getBean(this.resultImplClass, submission);
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            requestContext.put(this.resultPropName, result);
            requestContext.put(this.submissionPropName, submission);

            // noinspection ConstantConditions
            QName op = testcase.getOperation();

            try {
                result.setResponse(client.invoke(delegate, op, this.createRequest(submission))[0]);

                if (!result.hasMessages(SdcctIssueSeverity.FATAL) && !result.hasMessages(SdcctIssueSeverity.ERROR) && !testcase.isNegative()) {
                    result.setSuccess(true);
                }
            } catch (SoapFault e) {
                result.getMessages(SdcctIssueSeverity.INFORMATION)
                    .add(String.format(
                        "Please check that the web service response event payload contains a SOAP fault (message=%s) that corresponds to the associated testcase (id=%s) description.",
                        e.getMessage(), testcase.getId()));

                result.setFault(e);
            } catch (Exception e) {
                result.getMessages(SdcctIssueSeverity.ERROR)
                    .add(String.format("Unable to invoke IHE %s (endpointAddr=%s, op=%s): %s", this.roleName, endpointAddr, op, e.getMessage()));
            }

            return result;
        } finally {
            if (client != null) {
                client.getDelegate().destroy();
            }
        }
    }

    protected W createRequest(U submission) throws Exception {
        return this.createRequestInternal(submission);
    }

    protected abstract W createRequestInternal(U submission) throws Exception;
}
