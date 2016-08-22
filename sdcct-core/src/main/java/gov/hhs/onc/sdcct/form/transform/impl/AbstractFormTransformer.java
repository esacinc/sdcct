package gov.hhs.onc.sdcct.form.transform.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.form.SdcctForm;
import gov.hhs.onc.sdcct.form.transform.FormTransformer;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.transform.impl.SdcctConfiguration;
import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XdmDocumentDestination;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltCompiler;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltExecutable;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltTransformer;
import net.sf.saxon.trans.XPathException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

public abstract class AbstractFormTransformer<T> implements FormTransformer<T>{

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

    @Autowired
    private SdcctXsltCompiler xsltCompiler;

    @Autowired
    protected SdcctConfiguration config;

    private SdcctXsltExecutable xsltExec;

    protected AbstractFormTransformer(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, String name, ResourceSource src) {
        this.specType = specType;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
        this.name = name;
        this.src = src;
    }

    @Override
    public XdmDocument transform(T form) throws XPathException, SaxonApiException {
        XdmDocumentDestination output = new XdmDocumentDestination(this.config);
        XsltTransformer xsltTransformer = xsltExec.load();
        xsltTransformer.setDestination(output);
        xsltTransformer.setSource(((SdcctForm<?>)form).getSource());
        xsltTransformer.transform();
        return output.getXdmNode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.identifier == null) {
            this.identifier = name;
        }
        this.xsltExec = xsltCompiler.compile(this.src);
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
