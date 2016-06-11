package gov.hhs.onc.sdcct.form;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctForm<T> extends InitializingBean, NamedBean, SpecifiedBean {
    public T getBean();

    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public XdmDocument getDocument();

    public String getIdentifier();
}
