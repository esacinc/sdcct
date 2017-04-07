package gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.ihe.results.IheTestcaseResultHandler;
import gov.hhs.onc.sdcct.testcases.ihe.submissions.IheTestcaseSubmission;
import java.io.OutputStream;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.StaxOutEndingInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractServerIheTestcaseOutInterceptor<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>, W extends AbstractIheTestcaseOutCallback<T, U, V>>
    extends AbstractIheTestcaseInterceptor {
    @Autowired
    protected IheTestcaseResultHandler resultHandler;

    protected Class<V> resultClass;
    protected String resultPropName;

    protected AbstractServerIheTestcaseOutInterceptor(Class<V> resultClass, String resultPropName) {
        this(Phase.PRE_STREAM_ENDING, resultClass, resultPropName);
        this.addAfter(StaxOutEndingInterceptor.class.getName());
    }

    protected AbstractServerIheTestcaseOutInterceptor(String phase, Class<V> resultClass, String resultPropName) {
        super(phase);

        this.resultClass = resultClass;
        this.resultPropName = resultPropName;
    }

    @Override
    protected void handleMessageInternal(SoapMessage message) throws Exception {
        CacheAndWriteOutputStream cachedStream = new CacheAndWriteOutputStream(message.getContent(OutputStream.class));
        cachedStream.close();

        cachedStream.registerCallback(this.buildCallback(message));
        message.setContent(OutputStream.class, cachedStream);
    }

    protected abstract W buildCallback(Message msg);
}
