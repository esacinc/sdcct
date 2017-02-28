package gov.hhs.onc.sdcct.web.form.archiver.impl;

import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.ArchiveFormResponseType;
import gov.hhs.onc.sdcct.rfd.form.RfdForm;
import gov.hhs.onc.sdcct.rfd.impl.AnyXmlContentTypeImpl;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormArchiverTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.impl.AbstractIheTestcaseProcessor;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormArchiverTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormArchiverTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormArchiverTestcaseSubmission;
import gov.hhs.onc.sdcct.web.form.archiver.IheFormArchiverTestcaseProcessor;
import gov.hhs.onc.sdcct.ws.impl.JaxWsClient;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocumentDestination;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Fault;

public class IheFormArchiverTestcaseProcessorImpl
    extends AbstractIheTestcaseProcessor<IheFormArchiverTestcase, IheFormArchiverTestcaseSubmission, IheFormArchiverTestcaseResult>
    implements IheFormArchiverTestcaseProcessor {
    protected IheFormArchiverTestcaseProcessorImpl(String clientBeanName) {
        super(clientBeanName);
    }

    @Override
    public IheFormArchiverTestcaseResult process(IheFormArchiverTestcaseSubmission submission) {
        IheFormArchiverTestcase iheFormArchiverTestcase = submission.getTestcase();

        String endpointAddr = submission.getEndpointAddress();
        JaxWsClient client = (JaxWsClient) this.beanFactory.getBean(this.clientBeanName, endpointAddr);
        Client delegate = client.buildInvocationDelegate();
        BindingProvider bindingProvider = (BindingProvider) delegate;

        IheFormArchiverTestcaseResult iheFormArchiverTestcaseResult = new IheFormArchiverTestcaseResultImpl(submission);

        // noinspection ConstantConditions
        QName transaction = iheFormArchiverTestcase.getTransaction();

        // TODO: update
        RfdForm form = this.iheForms.stream().filter(iheForm -> iheForm.getIdentifier().equals(submission.getFormId())).findFirst().orElse(null);

        if (form != null) {
            try {
                AnyXmlContentType archiveFormRequestType = new AnyXmlContentTypeImpl().addAny(this.xmlCodec
                    .encode(form.getBean(), new XdmDocumentDestination(this.config).getReceiver(), null).getXdmNode().getDocument().getDocumentElement());
                ArchiveFormResponseType archiveFormResponseType = (ArchiveFormResponseType) client.invoke(delegate, transaction, archiveFormRequestType)[0];
                iheFormArchiverTestcaseResult.setResponse(archiveFormResponseType);
            } catch (Exception e) {
                if (e instanceof Fault) {
                    iheFormArchiverTestcaseResult.setFault((Fault) e);
                }

                iheFormArchiverTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                    .add(String.format("Unable to invoke IHE Form Archiver (endpointAddr=%s, transaction=%s): %s", endpointAddr, transaction, e.getMessage()));
            }
        } else {
            iheFormArchiverTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("Unable to find form (id=%s) from testcase (id=%s) submission.", submission.getFormId(), iheFormArchiverTestcase.getId()));
        }

        return iheFormArchiverTestcaseResult;
    }
}
