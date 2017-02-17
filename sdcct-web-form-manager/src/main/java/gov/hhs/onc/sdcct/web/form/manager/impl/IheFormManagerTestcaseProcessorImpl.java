package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.impl.MessageBeanImpl;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.manager.IheFormManagerTestcaseProcessor;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Fault;

public class IheFormManagerTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormManagerTestcase, IheFormManagerTestcaseSubmission, IheFormManagerTestcaseResult>
    implements IheFormManagerTestcaseProcessor {
    public IheFormManagerTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName);
    }

    @Override
    public IheFormManagerTestcaseResult process(IheFormManagerTestcaseSubmission submission) {
        IheFormManagerTestcase iheFormManagerTestcase = submission.getTestcase();
        // noinspection ConstantConditions
        RetrieveFormRequestType retrieveFormRequestType = iheFormManagerTestcase.getRequestParams();
        retrieveFormRequestType.getWorkflowData().setFormId(submission.getFormId());

        String endpointAddr = submission.getEndpointAddress();
        JaxWsClient client = (JaxWsClient) this.beanFactory.getBean(this.clientBeanName, endpointAddr);
        Client delegate = client.buildInvocationDelegate();
        BindingProvider bindingProvider = (BindingProvider) delegate;

        IheFormManagerTestcaseResult iheFormManagerTestcaseResult = new IheFormManagerTestcaseResultImpl(submission);

        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        requestContext.put(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, iheFormManagerTestcaseResult);
        requestContext.put(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_SUBMISSION, submission);

        // noinspection ConstantConditions
        QName transaction = iheFormManagerTestcase.getTransaction();

        try {
            RetrieveFormResponseType retrieveFormResponseType = (RetrieveFormResponseType) client.invoke(delegate, transaction, retrieveFormRequestType)[0];
            iheFormManagerTestcaseResult.setResponse(retrieveFormResponseType);
        } catch (Exception e) {
            if (e instanceof Fault) {
                iheFormManagerTestcaseResult.setFault((Fault) e);
            }

            iheFormManagerTestcaseResult.getMessages(SdcctIssueSeverity.ERROR).add(new MessageBeanImpl(SdcctIssueSeverity.ERROR,
                String.format("Unable to invoke IHE Form Manager (endpointAddr=%s, transaction=%s): %s", delegate.getEndpoint(), transaction, e.getMessage())));
        }

        return iheFormManagerTestcaseResult;
    }
}
