package gov.hhs.onc.sdcct.xml.jaxb;

import com.ctc.wstx.msv.W3CSchema;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import org.apache.ws.commons.schema.XmlSchemaCollection;

public interface JaxbContextInfo {
    public Set<Class<?>> getClasses();

    public void setClasses(Set<Class<?>> classes);

    public JAXBContext getContext();

    public void setContext(JAXBContext context);

    public Map<String, Object> getContextProperties();

    public void setContextProperties(Map<String, Object> contextProps);

    public Map<Class<?>, JaxbElementInfo<?>> getElementInfos();

    public void setElementInfos(Map<Class<?>, JaxbElementInfo<?>> elemInfos);

    public JaxbContextId getId();

    public Object[] getObjectFactories();

    public void setObjectFactories(Object ... objFactories);

    public XmlSchemaCollection getSchemaCollection();

    public void setSchemaCollection(XmlSchemaCollection schemaColl);

    public Map<String, XdmDocument> getSchemaDocuments();

    public void setSchemaDocuments(XdmDocument ... schemaDocs);

    public boolean hasValidationSchema();

    @Nullable
    public W3CSchema getValidationSchema();

    public void setValidationSchema(@Nullable W3CSchema validationSchema);
}
