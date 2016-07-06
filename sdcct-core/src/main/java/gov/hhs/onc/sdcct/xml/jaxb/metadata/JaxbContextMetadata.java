package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import com.sun.xml.bind.api.JAXBRIContext;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.validate.schema.impl.MsvValidationSchema;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface JaxbContextMetadata extends InitializingBean, JaxbMetadataComponent {
    public ContentPathBuilder getContentPathBuilder();

    public void setContentPathBuilder(ContentPathBuilder contentPathBuilder);

    public JAXBRIContext getContext();

    public Map<String, Object> getContextProperties();

    public void setContextProperties(Map<String, Object> contextProps);

    public XMLSchemaGrammar getSchemaGrammar();

    public Map<String, Package> getSchemaImplPackages();

    public void setSchemaImplPackages(Map<String, Package> schemaImplPkgs);

    public Map<String, Object> getSchemaObjectFactories();

    public void setSchemaObjectFactories(Map<String, Object> schemaObjFactories);

    public Map<String, Package> getSchemaPackages();

    public void setSchemaPackages(Map<String, Package> schemaPkgs);

    public Map<String, JaxbSchemaMetadata> getSchemas();

    public Map<String, ResourceSource> getSchemaSources();

    public void setSchemaSources(ResourceSource ... schemaSrcs);

    public MsvValidationSchema getValidationSchema();
}
