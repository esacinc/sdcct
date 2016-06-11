package gov.hhs.onc.sdcct.form.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctForm<T> implements SdcctForm<T> {
    @Autowired
    protected XmlCodec xmlCodec;

    protected SpecificationType specType;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;
    protected String name;
    protected XdmDocument doc;
    protected T bean;

    protected AbstractSdcctForm(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, String name, XdmDocument doc) {
        this.specType = specType;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
        this.name = name;
        this.doc = doc;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.bean = this.xmlCodec.decode(this.doc.getSource(), this.beanImplClass, null);
    }

    @Override
    public T getBean() {
        return this.bean;
    }

    @Override
    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }

    @Override
    public XdmDocument getDocument() {
        return this.doc;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
