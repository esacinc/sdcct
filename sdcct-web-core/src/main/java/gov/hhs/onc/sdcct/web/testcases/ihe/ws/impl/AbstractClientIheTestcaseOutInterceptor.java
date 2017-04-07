package gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.net.logging.RestEndpointType;
import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
import gov.hhs.onc.sdcct.ws.utils.SdcctRestEventUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.Phase;

public abstract class AbstractClientIheTestcaseOutInterceptor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    extends AbstractIheTestcaseInterceptor {
    protected String resultPropName;
    protected Class<V> resultClass;

    protected AbstractClientIheTestcaseOutInterceptor(String resultPropName, Class<V> resultClass) {
        this(Phase.MARSHAL, resultPropName, resultClass);
    }

    protected AbstractClientIheTestcaseOutInterceptor(String phase, String resultPropName, Class<V> resultClass) {
        super(phase);

        this.resultPropName = resultPropName;
        this.resultClass = resultClass;
    }

    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        V result = SdcctTestcaseUtils.addWsRequestEvent(message.getExchange().getOutMessage(), this.resultPropName, this.resultClass);

        result.setHttpRequestEvent(SdcctRestEventUtils.createHttpRequestEvent(message, SdcctPropertyNames.HTTP_CLIENT_TX_ID, RestEndpointType.CLIENT));
    }
}
