package gov.hhs.onc.sdcct.form;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import javax.annotation.Nullable;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctForm<T> extends InitializingBean, NamedBean, SpecifiedBean {
    public void build() throws Exception;

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

    public boolean isInternal();

    public void setInternal(boolean internal);

    public ResourceSource getSource();
}
