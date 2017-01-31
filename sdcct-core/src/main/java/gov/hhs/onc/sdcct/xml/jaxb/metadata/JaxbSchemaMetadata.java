package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.msv.grammar.xmlschema.XMLSchemaSchema;
import java.util.Map;

public interface JaxbSchemaMetadata extends JaxbContextMetadataComponent<XMLSchemaSchema> {
    public void addElement(JaxbElementMetadata<?> elem);

    public Map<String, JaxbElementMetadata<?>> getElementNames();

    public Map<String, String> getElementTypeNames();

    public Package getImplPackage();

    public Package getPackage();

    public String getPrefix();

    public void addType(JaxbTypeMetadata<?, ?> type);

    public Map<Class<?>, JaxbTypeMetadata<?, ?>> getTypeBeanClasses();

    public Map<String, JaxbTypeMetadata<?, ?>> getTypeNames();

    public String getXpathPrefix();
}
