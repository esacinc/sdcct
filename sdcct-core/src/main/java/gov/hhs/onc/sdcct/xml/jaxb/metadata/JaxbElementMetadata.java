package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.msv.grammar.xmlschema.ElementDeclExp;

public interface JaxbElementMetadata<T> extends JaxbSchemaMetadataComponent<ElementDeclExp> {
    public Class<?> getScope();

    public JaxbComplexTypeMetadata<T> getType();
}
