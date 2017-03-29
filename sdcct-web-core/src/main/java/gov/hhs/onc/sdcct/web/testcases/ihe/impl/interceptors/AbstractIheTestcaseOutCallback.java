package gov.hhs.onc.sdcct.web.testcases.ihe.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.testcases.results.ihe.IheTestcaseResult;
import gov.hhs.onc.sdcct.testcases.submissions.ihe.IheTestcaseSubmission;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;

public abstract class AbstractIheTestcaseOutCallback<T extends IheTestcase, U extends IheTestcaseSubmission<T>, V extends IheTestcaseResult<T, U>>
    implements CachedOutputStreamCallback {
    protected Message msg;
    protected Class<V> resultClass;
    protected String resultPropName;

    protected AbstractIheTestcaseOutCallback(Message msg, Class<V> resultClass, String resultPropName) {
        this.msg = msg;
        this.resultClass = resultClass;
        this.resultPropName = resultPropName;
    }

    @Override
    public void onClose(CachedOutputStream cachedStream) {
        try {
            this.onCloseInternal(((CacheAndWriteOutputStream) cachedStream));
        } catch (Fault e) {
            throw e;
        } catch (Exception e) {
            throw new Fault(e);
        }
    }

    @Override
    public void onFlush(CachedOutputStream os) {
    }

    protected abstract void onCloseInternal(CacheAndWriteOutputStream cachedStream) throws Exception;
}
