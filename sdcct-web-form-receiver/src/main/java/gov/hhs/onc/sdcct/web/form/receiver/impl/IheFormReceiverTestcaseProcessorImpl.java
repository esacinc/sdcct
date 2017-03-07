package gov.hhs.onc.sdcct.web.form.receiver.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.SubmitFormResponseType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormReceiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormReceiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormReceiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormReceiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.receiver.IheFormReceiverTestcaseProcessor;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Fault;

public class IheFormReceiverTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormReceiverTestcase, IheFormReceiverTestcaseSubmission, IheFormReceiverTestcaseResult>
    implements IheFormReceiverTestcaseProcessor {
    public IheFormReceiverTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName);
    }

    @Override
    public IheFormReceiverTestcaseResult process(IheFormReceiverTestcaseSubmission submission) {
        IheFormReceiverTestcase iheFormReceiverTestcase = submission.getTestcase();

        String endpointAddr = submission.getEndpointAddress();
        JaxWsClient client = (JaxWsClient) this.beanFactory.getBean(this.clientBeanName, endpointAddr);
        Client delegate = client.buildInvocationDelegate();
        BindingProvider bindingProvider = (BindingProvider) delegate;

        IheFormReceiverTestcaseResult iheFormReceiverTestcaseResult = new IheFormReceiverTestcaseResultImpl(submission);

        // noinspection ConstantConditions
        QName transaction = iheFormReceiverTestcase.getTransaction();

        // TODO: update
        RfdForm form = this.iheForms.stream().filter(iheForm -> iheForm.getIdentifier().equals(submission.getFormId())).findFirst().orElse(null);

        if (form != null) {
            try {
                AnyXmlContentType submitFormRequestType = new AnyXmlContentTypeImpl().addAny(this.xmlCodec
                    .encode(form.getBean(), new XdmDocumentDestination(this.config).getReceiver(), null).getXdmNode().getDocument().getDocumentElement());
                SubmitFormResponseType submitFormResponseType = (SubmitFormResponseType) client.invoke(delegate, transaction, submitFormRequestType)[0];
                iheFormReceiverTestcaseResult.setResponse(submitFormResponseType);
            } catch (Exception e) {
                if (e instanceof Fault) {
                    iheFormReceiverTestcaseResult.setFault((Fault) e);
                }

                iheFormReceiverTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                    .add(String.format("Unable to invoke IHE Form Receiver (endpointAddr=%s, transaction=%s): %s", endpointAddr, transaction, e.getMessage()));
            }
        } else {
            iheFormReceiverTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("Unable to find form (id=%s) from testcase (id=%s) submission.", submission.getFormId(), iheFormReceiverTestcase.getId()));
        }

        return iheFormReceiverTestcaseResult;
    }
}
