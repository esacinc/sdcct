package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.xml.bind.api.JAXBRIContext;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.validate.impl.MsvXmlSchemaImpl;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface JaxbContextMetadata extends InitializingBean, JaxbMetadataComponent {
    public JAXBRIContext getContext();

    public Map<String, Object> getContextProperties();

    public void setContextProperties(Map<String, Object> contextProps);

    public Map<String, Package> getSchemaImplPackages();

    public void setSchemaImplPackages(Map<String, Package> schemaImplPkgs);

    public Map<String, Object> getSchemaObjectFactories();

    public void setSchemaObjectFactories(Map<String, Object> schemaObjFactories);

    public Map<String, Package> getSchemaPackages();

    public void setSchemaPackages(Map<String, Package> schemaPkgs);

    public Map<String, String> getSchemaPrefixes();

    public void setSchemaPrefixes(Map<String, String> schemaPrefixes);

    public Map<String, JaxbSchemaMetadata> getSchemas();

    public Map<String, ResourceSource> getSchemaSources();

    public void setSchemaSources(ResourceSource ... schemaSrcs);

    public Map<String, String> getSchemaXpathPrefixes();

    public void setSchemaXpathPrefixes(Map<String, String> schemaXpathPrefixes);

    public MsvXmlSchemaImpl getValidationSchema();
}
