package gov.hhs.onc.sdcct.form.transform;

import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import javax.annotation.Nullable;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.trans.XPathException;
import org.springframework.beans.factory.InitializingBean;

public interface FormTransformer<T> extends InitializingBean, SpecifiedBean {
    public boolean hasDocument();

    @Nullable
    public XdmDocument getDocument();

    public void setDocument(@Nullable XdmDocument doc);

    public XdmDocument transform(T sdcctForm) throws XPathException, SaxonApiException;

    public ResourceSource getSource();
}
