package gov.hhs.onc.sdcct.web.form.manager.rfd.ws.impl.interceptors;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.beans.impl.MessageBeanImpl;
import gov.hhs.onc.sdcct.rfd.EncodedResponse;
import gov.hhs.onc.sdcct.rfd.RetrieveFormRequestType;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormRequestTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlQnames;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsaActions;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.ihe.impl.IheFormManagerTestcaseResultImpl;
import gov.hhs.onc.sdcct.testcases.rfd.impl.interceptors.AbstractIheTestcaseInterceptor;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.impl.IheFormManagerTestcaseSubmissionImpl;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.validate.testcases.rfd.IheTestcaseValidationException;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import java.util.List;
import javax.xml.transform.dom.DOMSource;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component("interceptorServerIheFormManagerTestcaseIn")
public class ServerIheFormManagerTestcaseInInterceptor extends AbstractIheTestcaseInterceptor<IheFormManagerTestcase> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerIheFormManagerTestcaseInInterceptor.class);

    @Autowired
    private List<IheFormManagerTestcase> iheFormManagerTestcases;

    public ServerIheFormManagerTestcaseInInterceptor() {
        super(Phase.UNMARSHAL);
    }

    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        LOGGER.info(String.format("Handling message in interceptor (class=%s).", this.getClass().getName()));

        Message inMsg = message.getExchange().getInMessage();
        String soapActionPropValue = SdcctWsPropertyUtils.getProperty(inMsg, SoapBindingConstants.SOAP_ACTION);

        if (RfdWsaActions.RETRIEVE_FORM.equals(soapActionPropValue)) {
            try {
                CachedOutputStream cachedOutputStream = message.getContent(CachedOutputStream.class);
                cachedOutputStream.close();

                XdmDocument doc = this.docBuilder.build(new ByteArraySource(cachedOutputStream.getBytes()));
                Element docElem = doc.getDocument().getDocumentElement();
                Element retrieveFormRequestElem =
                    DOMUtils.getFirstChildWithName(DOMUtils.getFirstChildWithName(docElem, SOAP12Constants.QNAME_SOAP_BODY), RfdWsXmlQnames.RETRIEVE_FORM_REQ);

                RetrieveFormRequestType retrieveFormRequest =
                    this.xmlCodec.decode(this.xmlCodec.encode(new DOMSource(retrieveFormRequestElem), null), RetrieveFormRequestTypeImpl.class, null);
                EncodedResponse actualEncodedResponse = retrieveFormRequest.getWorkflowData().getEncodedResponse();

                IheFormManagerTestcase iheFormManagerTestcase = null;

                for (IheFormManagerTestcase testcase : this.iheFormManagerTestcases) {
                    if (!testcase.isSdcctInitiated()) {
                        EncodedResponse expectedEncodedResponse = testcase.getRequestParams().getWorkflowData().getEncodedResponse();

                        if (expectedEncodedResponse.getResponseContentType().equals(actualEncodedResponse.getResponseContentType()) &&
                            expectedEncodedResponse.getValue() == actualEncodedResponse.getValue()) {
                            iheFormManagerTestcase = testcase;

                            break;
                        }
                    }
                }

                if (iheFormManagerTestcase != null) {
                    IheFormManagerTestcaseSubmission iheFormManagerTestcaseSubmission = new IheFormManagerTestcaseSubmissionImpl(iheFormManagerTestcase,
                        SdcctWsPropertyUtils.getProperty(inMsg, Message.REQUEST_URL), retrieveFormRequest.getWorkflowData().getFormId());
                    IheFormManagerTestcaseResult iheFormManagerTestcaseResult = new IheFormManagerTestcaseResultImpl(iheFormManagerTestcaseSubmission);

                    message.put(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT, iheFormManagerTestcaseResult);

                    iheFormManagerTestcaseResult = SdcctTestcaseUtils.addWsRequestEvent(inMsg, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT,
                        IheFormManagerTestcaseResult.class);

                    this.validateRetrieveFormRequest(iheFormManagerTestcase, iheFormManagerTestcaseResult, retrieveFormRequest);
                }
            } catch (Exception e) {
                throw new IheTestcaseValidationException("Unable to parse request for testcase", e);
            }
        }
    }

    private void validateRetrieveFormRequest(IheFormManagerTestcase iheFormManagerTestcase, IheFormManagerTestcaseResult iheFormManagerTestcaseResult,
        RetrieveFormRequestType actualRequest) {
        RetrieveFormRequestType expectedRequest = iheFormManagerTestcase.getRequestParams();

        EncodedResponse actualEncodedResponse = actualRequest.getWorkflowData().getEncodedResponse(),
            expectedEncodedResponse = expectedRequest.getWorkflowData().getEncodedResponse();

        if (expectedEncodedResponse.getValue() != actualEncodedResponse.getValue()) {
            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(new MessageBeanImpl(SdcctIssueSeverity.ERROR,
                    String.format("%s for testcase (id=%s) does not contain the expected encodedResponse value (expected=%s, actual=%s).",
                        RfdWsXmlNames.RETRIEVE_FORM_REQ, iheFormManagerTestcase.getId(), expectedEncodedResponse.getValue(),
                        actualEncodedResponse.getValue())));
        }

        if (!expectedEncodedResponse.getResponseContentType().equals(actualEncodedResponse.getResponseContentType())) {
            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(new MessageBeanImpl(SdcctIssueSeverity.ERROR,
                    String.format("%s for testcase (id=%s) does not contain the expected responseContentType value (expected=%s, actual=%s).",
                        RfdWsXmlNames.RETRIEVE_FORM_REQ, iheFormManagerTestcase.getId(), expectedEncodedResponse.getResponseContentType(),
                        actualEncodedResponse.getResponseContentType())));
        }
    }
}
