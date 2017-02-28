package gov.hhs.onc.sdcct.web.form.manager.rfd.ws.impl.interceptors;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import gov.hhs.onc.sdcct.rfd.AnyXmlContentType;
import gov.hhs.onc.sdcct.rfd.RetrieveFormResponseType;
import gov.hhs.onc.sdcct.rfd.impl.RetrieveFormResponseTypeImpl;
import gov.hhs.onc.sdcct.rfd.ws.RfdWsXmlNames;
import gov.hhs.onc.sdcct.sdc.SdcRetrieveFormPackageType;
import gov.hhs.onc.sdcct.sdc.XmlPackage;
import gov.hhs.onc.sdcct.testcases.SdcctTestcasePropertyNames;
import gov.hhs.onc.sdcct.testcases.ihe.IheFormManagerTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheFormManagerTestcaseResult;
import gov.hhs.onc.sdcct.testcases.rfd.impl.interceptors.AbstractIheTestcaseInterceptor;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheFormManagerTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.validate.testcases.rfd.IheTestcaseValidationException;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.dom.DOMSource;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component("interceptorClientIheFormManagerTestcaseIn")
public class ClientIheFormManagerTestcaseInInterceptor extends AbstractIheTestcaseInterceptor<IheFormManagerTestcase> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClientIheFormManagerTestcaseInInterceptor.class);

    public ClientIheFormManagerTestcaseInInterceptor() {
        super(Phase.POST_PROTOCOL);
    }

    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        LOGGER.info(String.format("Handling message in interceptor (class=%s).", this.getClass().getName()));

        Message inMsg = message.getExchange().getInMessage();
        Object submissionPropValue = inMsg.getContextualProperty(SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_SUBMISSION);

        if (submissionPropValue != null) {
            IheFormManagerTestcaseSubmission submission = (IheFormManagerTestcaseSubmission) submissionPropValue;
            IheFormManagerTestcase testcase = submission.getTestcase();
            // noinspection ConstantConditions
            String testcaseId = testcase.getId();

            IheFormManagerTestcaseResult result = SdcctTestcaseUtils.addWsResponseEvent(inMsg, SdcctTestcasePropertyNames.IHE_FORM_MANAGER_TESTCASE_RESULT,
                IheFormManagerTestcaseResult.class, inMsg);

            try {
                CachedOutputStream cachedOutputStream = message.getContent(CachedOutputStream.class);
                cachedOutputStream.close();

                XdmDocument doc = this.docBuilder.build(new ByteArraySource(cachedOutputStream.getBytes()));
                Element docElem = doc.getDocument().getDocumentElement();
                Element bodyElem = DOMUtils.getFirstChildWithName(docElem, SOAP12Constants.QNAME_SOAP_BODY);
                Element soapFaultElem = DOMUtils.getFirstChildWithName(bodyElem, SOAP12Constants.QNAME_SOAP_FAULT);

                if (soapFaultElem == null) {
                    RetrieveFormResponseType response =
                        this.xmlCodec.decode(this.xmlCodec.encode(new DOMSource(bodyElem.getFirstChild()), null), RetrieveFormResponseTypeImpl.class, null);

                    if (response != null) {
                        if (testcase.isNegative()) {
                            result.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("%s was unexpectedly returned for testcase (id=%s).", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));
                        } else {
                            this.validateRetrieveFormResponse(submission, testcase, result, response);
                        }
                    } else {
                        if (!testcase.isNegative()) {
                            result.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("%s was not expected to be returned for testcase (id=%s).", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));
                        }
                    }
                } else {
                    if (!testcase.isNegative()) {
                        Element soapFaultReasonElem = DOMUtils.getFirstChildWithName(soapFaultElem, SOAP12Constants.QNAME_FAULT_REASON);

                        result.getMessages().get(SdcctIssueSeverity.ERROR)
                            .add(String.format("Unexpected SOAP fault occurred for testcase (id=%s): %s", testcaseId,
                                (soapFaultReasonElem != null)
                                    ? DOMUtils.getContent(DOMUtils.getFirstChildWithName(soapFaultReasonElem, SOAP12Constants.QNAME_FAULT_REASON_TEXT))
                                    : "No SOAP fault reason text provided"));
                    }
                }
            } catch (Exception e) {
                throw new IheTestcaseValidationException(String.format("Unable to parse response for testcase (id=%s)", testcaseId), e);
            }
        }
    }

    private void validateRetrieveFormResponse(IheFormManagerTestcaseSubmission submission, IheFormManagerTestcase iheFormManagerTestcase,
        IheFormManagerTestcaseResult iheFormManagerTestcaseResult, RetrieveFormResponseType actualResponse) {
        RetrieveFormResponseType expectedResponse = iheFormManagerTestcase.getResponse();
        String testcaseId = iheFormManagerTestcase.getId();

        // noinspection ConstantConditions
        if (!actualResponse.getContentType().equals(expectedResponse.getContentType())) {
            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("%s contains unexpected contentType (expected=%s, actual=%s).", RfdWsXmlNames.RETRIEVE_FORM_RESP,
                    expectedResponse.getContentType(), actualResponse.getContentType()));
        }

        if (!actualResponse.getForm().hasContent()) {
            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                .add(String.format("%s form does not contain content.", RfdWsXmlNames.RETRIEVE_FORM_RESP));
        } else {
            Object content = actualResponse.getForm().getContent();

            switch (expectedResponse.getContentType()) {
                case SdcctMediaTypes.APP_XML_SDC_VALUE:
                    if (content instanceof AnyXmlContentType) {
                        SdcRetrieveFormPackageType packageType =
                            (SdcRetrieveFormPackageType) ((JAXBElement) ((AnyXmlContentType) content).getAny().get(0)).getValue();

                        String expectedFormId = submission.getFormId();
                        String actualFormId = ((XmlPackage) packageType.getContent().get(0)).getFormDesign().getId();

                        if (expectedFormId != null && !expectedFormId.equals(actualFormId)) {
                            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("Form ID (%s) in %s does not equal form ID (%s) requested in submission.", actualFormId,
                                    RfdWsXmlNames.RETRIEVE_FORM_RESP, expectedFormId));
                        }
                    } else {
                        iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR).add(
                            String.format("%s for testcase (id=%s) does not contain the Structured element.", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));
                    }

                    break;

                case SdcctMediaTypes.TEXT_HTML_SDC_VALUE:
                    if (!(content instanceof AnyXmlContentType)) {
                        iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR).add(
                            String.format("%s for testcase (id=%s) does not contain the Structured element.", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));
                    }

                    break;

                case "URL":
                    if (!(content instanceof String)) {
                        iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                            .add(String.format("%s for testcase (id=%s) does not contain the URL element.", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));
                    } else {
                        String expectedFormId = submission.getFormId();
                        String actualFormId = (String) content;

                        if (expectedFormId != null && !expectedFormId.equals(actualFormId)) {
                            iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("Form ID (%s) in %s does not equal form ID (%s) requested in submission.", actualFormId,
                                    RfdWsXmlNames.RETRIEVE_FORM_RESP, expectedFormId));
                        }
                    }

                    break;

                default:
                    iheFormManagerTestcaseResult.getMessages().get(SdcctIssueSeverity.ERROR)
                        .add(String.format("%s for testcase (id=%s) does not contain a valid contentType.", RfdWsXmlNames.RETRIEVE_FORM_RESP, testcaseId));

                    break;
            }
        }
    }
}
