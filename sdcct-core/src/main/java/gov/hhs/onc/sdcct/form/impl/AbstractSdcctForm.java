package gov.hhs.onc.sdcct.form.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctForm<T> implements SdcctForm<T> {
    @Autowired
    protected XmlCodec xmlCodec;

    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    protected SpecificationType specType;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;
    protected String name;
    protected ResourceSource src;
    protected String identifier;
    protected boolean internal;
    protected XdmDocument doc;
    protected T bean;

    protected AbstractSdcctForm(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, String name, ResourceSource src) {
        this.specType = specType;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
        this.name = name;
        this.src = src;
    }

    @Override
    public void build() throws Exception {
        this.bean = this.xmlCodec.decode((this.doc = this.docBuilder.build(this.src)).getUnderlyingNode(), this.beanImplClass, null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.identifier == null) {
            this.identifier = name;
        }

        if (this.internal) {
            this.build();
        }
    }

    @Override
    public boolean hasBean() {
        return (this.bean != null);
    }

    @Nullable
    @Override
    public T getBean() {
        return this.bean;
    }

    @Override
    public void setBean(@Nullable T bean) {
        this.bean = bean;
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
    public boolean hasDocument() {
        return (this.doc != null);
    }

    @Override
    public XdmDocument getDocument() {
        return this.doc;
    }

    @Override
    public void setDocument(@Nullable XdmDocument doc) {
        this.doc = doc;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean isInternal() {
        return this.internal;
    }

    @Override
    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ResourceSource getSource() {
        return this.src;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
