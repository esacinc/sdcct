package gov.hhs.onc.sdcct.validate.schematron;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.xslt.impl.SdcctXsltExecutable;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctSchematron extends IdentifiedBean, InitializingBean, NamedBean {
    public XdmDocument getDocument();

    public void setDocument(XdmDocument doc);

    public String getQueryBinding();

    public void setQueryBinding(String queryBinding);

    public Map<String, String> getSchemaNamespaces();

    public String getSchemaVersion();

    public void setSchemaVersion(String schemaVersion);

    public SdcctXsltExecutable getSchemaXsltExecutable();

    public SdcctXsltExecutable[] getXsltExecutables();

    public void setXsltExecutables(SdcctXsltExecutable ... xsltExecs);
}
