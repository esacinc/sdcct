package gov.hhs.onc.sdcct.testcases.ihe.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import gov.hhs.onc.sdcct.testcases.utils.SdcctTestcaseUtils;
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
        SdcctTestcaseUtils.addWsRequestEvent(message.getExchange().getOutMessage(), this.resultPropName, this.resultClass);
    }
}
