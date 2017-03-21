package gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.validate.testcases.rfd.IheTestcaseValidationException;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import javax.xml.transform.dom.DOMSource;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;

public abstract class AbstractClientIheTestcaseInInterceptor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>, W>
    extends AbstractIheTestcaseInterceptor {
    protected Class<W> responseClass;
    protected Class<? extends W> responseImplClass;
    protected Class<V> resultClass;
    protected String resultPropName;
    protected String submissionPropName;
    protected String wsResponseName;

    protected AbstractClientIheTestcaseInInterceptor(Class<W> responseClass, Class<? extends W> responseImplClass, Class<V> resultClass, String resultPropName,
        String submissionPropName, String wsResponseName) {
        this(Phase.POST_PROTOCOL, responseClass, responseImplClass, resultClass, resultPropName, submissionPropName, wsResponseName);
    }

    protected AbstractClientIheTestcaseInInterceptor(String phase, Class<W> responseClass, Class<? extends W> responseImplClass, Class<V> resultClass,
        String resultPropName, String submissionPropName, String wsResponseName) {
        super(phase);

        this.responseClass = responseClass;
        this.responseImplClass = responseImplClass;
        this.resultClass = resultClass;
        this.resultPropName = resultPropName;
        this.submissionPropName = submissionPropName;
        this.wsResponseName = wsResponseName;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        Message inMsg = message.getExchange().getInMessage();
        Object submissionPropValue = inMsg.getContextualProperty(this.submissionPropName);

        if (submissionPropValue != null) {
            U submission = (U) submissionPropValue;
            T testcase = submission.getTestcase();
            // noinspection ConstantConditions
            String testcaseId = testcase.getId();

            V result = SdcctTestcaseUtils.addWsResponseEvent(inMsg, this.resultPropName, this.resultClass, inMsg);

            try {
                CachedOutputStream cachedOutputStream = message.getContent(CachedOutputStream.class);
                cachedOutputStream.close();

                XdmDocument doc = this.docBuilder.build(new ByteArraySource(cachedOutputStream.getBytes()));
                Element docElem = doc.getDocument().getDocumentElement();
                Element bodyElem = DOMUtils.getFirstChildWithName(docElem, SOAP12Constants.QNAME_SOAP_BODY);
                Element soapFaultElem = DOMUtils.getFirstChildWithName(bodyElem, SOAP12Constants.QNAME_SOAP_FAULT);

                if (soapFaultElem == null) {
                    W response = this.xmlCodec.decode(this.xmlCodec.encode(new DOMSource(bodyElem.getFirstChild()), null), this.responseImplClass, null);

                    if (response != null) {
                        if (testcase.isNegative()) {
                            result.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("%s was unexpectedly returned for testcase (id=%s).", this.wsResponseName, testcaseId));
                        } else {
                            this.validateResponse(testcase, submission, result, response);
                        }
                    } else {
                        if (!testcase.isNegative()) {
                            result.getMessages().get(SdcctIssueSeverity.ERROR)
                                .add(String.format("%s was not expected to be returned for testcase (id=%s).", this.wsResponseName, testcaseId));
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

    protected void validateResponse(T testcase, U submission, V result, W response) {
        this.validateResponseInternal(testcase, submission, result, response);
    }

    protected abstract void validateResponseInternal(T testcase, U submission, V result, W response);
}
