package gov.hhs.onc.sdcct.web.testcases.ihe.ws.impl;

import gov.hhs.onc.sdcct.logging.impl.TxIdGenerator;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import javax.annotation.Resource;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractIheTestcaseInterceptor extends AbstractPhaseInterceptor<SoapMessage> implements BeanFactoryAware {
    @Resource(name = "txIdGenTestcaseProcess")
    protected TxIdGenerator txIdGen;

    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    @Autowired
    protected XmlCodec xmlCodec;

    protected BeanFactory beanFactory;

    protected AbstractIheTestcaseInterceptor(String phase) {
        super(phase);
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        try {
            this.handleMessageInternal(message);
        } catch (Exception e) {
            throw new Fault(e);
        }
    }

    protected abstract void handleMessageInternal(SoapMessage message) throws Exception;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
