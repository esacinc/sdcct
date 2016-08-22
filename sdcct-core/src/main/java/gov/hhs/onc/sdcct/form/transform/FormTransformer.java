package gov.hhs.onc.sdcct.form.transform;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.trans.XPathException;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Nullable;

public interface FormTransformer<T> extends InitializingBean, NamedBean, SpecifiedBean {

    public boolean hasBean();

    @Nullable
    public T getBean();

    public void setBean(@Nullable T bean);

    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public boolean hasDocument();

    @Nullable
    public XdmDocument getDocument();

    public void setDocument(@Nullable XdmDocument doc);

    public String getIdentifier();

    public void setIdentifier(String identifier);

    public XdmDocument transform(T sdcctForm) throws XPathException, SaxonApiException;
    public ResourceSource getSource();
}
