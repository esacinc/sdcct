package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.sun.msv.grammar.xmlschema.ElementDeclExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbElementMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import javax.xml.namespace.QName;

public class JaxbElementMetadataImpl<T> extends AbstractJaxbSchemaMetadataComponent<ElementDeclExp> implements JaxbElementMetadata<T> {
    private JaxbComplexTypeMetadata<T> type;
    private Class<?> scope;

    public JaxbElementMetadataImpl(JaxbContextMetadata context, JaxbSchemaMetadata schema, ElementDeclExp expr, String name, QName qname, boolean abztract,
        Class<?> scope, JaxbComplexTypeMetadata<T> type) {
        super(context, schema, expr, name, qname, abztract);

        this.scope = scope;
        this.type = type;
    }

    @Override
    public Class<?> getScope() {
        return this.scope;
    }

    @Override
    public JaxbComplexTypeMetadata<T> getType() {
        return this.type;
    }
}
