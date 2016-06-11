package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.ctc.wstx.msv.W3CSchema;
import com.sun.msv.grammar.xmlschema.XMLSchemaGrammar;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import org.springframework.beans.factory.InitializingBean;

public interface JaxbContextMetadata extends InitializingBean, JaxbMetadataComponent {
    public JAXBContext getContext();

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

    public W3CSchema getValidationSchema();
}
