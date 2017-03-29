package gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors;

import com.sun.xml.ws.encoding.soap.SOAP12Constants;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.transform.content.ContentCodec;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.validate.testcases.rfd.IheTestcaseValidationException;
import gov.hhs.onc.sdcct.web.tomcat.impl.TomcatRequest;
import gov.hhs.onc.sdcct.web.tomcat.utils.SdcctHttpEventUtils;
import gov.hhs.onc.sdcct.web.tomcat.utils.SdcctTomcatUtils;
import gov.hhs.onc.sdcct.ws.WsDirection;
import gov.hhs.onc.sdcct.ws.WsMessageType;
import gov.hhs.onc.sdcct.ws.logging.WsRequestEvent;
import gov.hhs.onc.sdcct.ws.logging.impl.SdcctLoggingFeature;
import gov.hhs.onc.sdcct.ws.logging.impl.WsRequestEventImpl;
import gov.hhs.onc.sdcct.ws.utils.SdcctRestEventUtils;
import gov.hhs.onc.sdcct.ws.utils.SdcctWsPropertyUtils;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnegative;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;
import org.apache.cxf.binding.soap.SoapBindingConstants;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;

public abstract class AbstractServerIheTestcaseInInterceptor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>, W>
    extends AbstractIheTestcaseInterceptor implements InitializingBean {
    @Autowired
    protected List<ContentCodec<?, ?>> codecs;

    protected Map<SdcctContentType, ContentCodec<?, ?>> contentTypeCodecs;
    protected Class<? extends W> requestImplClass;
    protected QName requestQName;
    protected String resultPropName;
    protected Class<V> resultClass;
    protected Class<? extends V> resultImplClass;
    protected Class<? extends U> submissionImplClass;
    protected String wsaAction;
    protected WsMessageType wsMessageType;

    protected AbstractServerIheTestcaseInInterceptor(Class<? extends W> requestImplClass, QName requestQName, String resultPropName, Class<V> resultClass,
        Class<? extends V> resultImplClass, Class<? extends U> submissionImplClass, String wsaAction) {
        this(Phase.UNMARSHAL, requestImplClass, requestQName, resultPropName, resultClass, resultImplClass, submissionImplClass, wsaAction);
    }

    protected AbstractServerIheTestcaseInInterceptor(String phase, Class<? extends W> requestImplClass, QName requestQName, String resultPropName,
        Class<V> resultClass, Class<? extends V> resultImplClass, Class<? extends U> submissionImplClass, String wsaAction) {
        super(phase);

        this.requestImplClass = requestImplClass;
        this.requestQName = requestQName;
        this.resultPropName = resultPropName;
        this.resultClass = resultClass;
        this.resultImplClass = resultImplClass;
        this.submissionImplClass = submissionImplClass;
        this.wsaAction = wsaAction;
        this.wsMessageType = WsMessageType.SOAP;
    }

    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        long submittedTimestamp = System.currentTimeMillis();
        String txId = this.txIdGen.generateId(submittedTimestamp).toString();
        Message inMsg = message.getExchange().getInMessage();
        String soapActionPropValue = SdcctWsPropertyUtils.getProperty(inMsg, SoapBindingConstants.SOAP_ACTION);

        V result = null;

        if (this.wsaAction.equals(soapActionPropValue)) {
            CachedOutputStream cachedOutputStream = message.getContent(CachedOutputStream.class);
            cachedOutputStream.close();

            try {
                XdmDocument doc = this.docBuilder.build(new ByteArraySource(cachedOutputStream.getBytes()));

                Element docElem = doc.getDocument().getDocumentElement();
                Element requestElem =
                    DOMUtils.getFirstChildWithName(DOMUtils.getFirstChildWithName(docElem, SOAP12Constants.QNAME_SOAP_BODY), this.requestQName);

                W request = this.xmlCodec.decode(this.xmlCodec.encode(new DOMSource(requestElem), null), this.requestImplClass, null);

                result = this.processRequest(request, message, submittedTimestamp, txId);

                U submission = result.getSubmission();
                T testcase = submission != null ? submission.getTestcase() : null;

                if (testcase != null) {
                    this.validateRequest(testcase, result, request);
                } else {
                    result.getMessages(SdcctIssueSeverity.INFORMATION).add(
                        "The testcase associated with the web service request could not be determined. Please check that the web service response event payload corresponds to the expected response for the intended testcase.");
                }
            } catch (Exception e) {
                result = this.createResult(null, message, null, submittedTimestamp, txId);

                throw new IheTestcaseValidationException(String.format("Unable to parse request for testcase (SOAP action=%s).", soapActionPropValue), e);
            } finally {
                WsRequestEvent wsRequestEvent = new WsRequestEventImpl();
                wsRequestEvent.setDirection(WsDirection.INBOUND);
                wsRequestEvent.setEndpointType(RestEndpointType.SERVER);
                wsRequestEvent.setMessageType(this.wsMessageType);
                wsRequestEvent.setTxId(SdcctLoggingFeature.buildTxId(message.getExchange(), message, wsRequestEvent, SdcctPropertyNames.WS_SERVER_TX_ID));

                SdcctRestEventUtils.processEvent(message.getExchange(), message, wsRequestEvent, this.wsMessageType, this.docBuilder, this.contentTypeCodecs,
                    cachedOutputStream.getBytes());

                if (result == null) {
                    result = this.createResult(null, message, null, submittedTimestamp, txId);
                }

                result.setWsRequestEvent(wsRequestEvent);

                TomcatRequest httpServletReq =
                    SdcctTomcatUtils.unwrapRequest(SdcctWsPropertyUtils.getProperty(message, AbstractHTTPDestination.HTTP_REQUEST, HttpServletRequest.class));

                result.setHttpRequestEvent(SdcctHttpEventUtils.createHttpRequestEvent(httpServletReq));

                result.setProcessedTimestamp(System.currentTimeMillis());
            }
        }
    }

    protected abstract T findTestcase(W request, List<T> testcases, String formId);

    protected V createResult(T testcase, SoapMessage message, String formId, @Nonnegative long submittedTimestamp, String txId) {
        Message inMsg = message.getExchange().getInMessage();

        U testcaseSubmission =
            this.beanFactory.getBean(this.submissionImplClass, testcase, SdcctWsPropertyUtils.getProperty(inMsg, Message.REQUEST_URL), formId);
        testcaseSubmission.setSubmittedTimestamp(submittedTimestamp);
        testcaseSubmission.setTxId(txId);

        V testcaseResult = this.beanFactory.getBean(this.resultImplClass, testcaseSubmission);

        testcaseResult.setTxId(txId);

        inMsg.put(this.resultPropName, testcaseResult);

        return testcaseResult;
    }

    protected abstract V processRequest(W request, SoapMessage message, @Nonnegative long submittedTimestamp, String txId) throws Exception;

    protected abstract void validateRequest(T testcase, V testcaseResult, W request);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.contentTypeCodecs = this.codecs.stream().collect(SdcctStreamUtils.toMap(ContentCodec::getType, Function.identity(), HashMap::new));
    }
}
