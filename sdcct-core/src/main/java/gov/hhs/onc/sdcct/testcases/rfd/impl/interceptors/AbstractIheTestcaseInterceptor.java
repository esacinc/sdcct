package gov.hhs.onc.sdcct.testcases.rfd.impl.interceptors;

import gov.hhs.onc.sdcct.testcases.ihe.IheTestcase;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractIheTestcaseInterceptor<T extends IheTestcase> extends AbstractPhaseInterceptor<SoapMessage> {
    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    @Autowired
    protected XmlCodec xmlCodec;

    protected AbstractIheTestcaseInterceptor(String phase) {
        super(phase);
    }

    protected abstract void handleMessageInternal(SoapMessage message) throws Exception;

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        try {
            this.handleMessageInternal(message);
        } catch (Exception e) {
            throw new Fault(e);
        }
    }
}
