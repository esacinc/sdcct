package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.sun.msv.grammar.xmlschema.ComplexTypeExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public class JaxbComplexTypeMetadataImpl<T> extends AbstractJaxbTypeMetadata<ComplexTypeExp, T> implements JaxbComplexTypeMetadata<T> {
    public JaxbComplexTypeMetadataImpl(JaxbContextMetadata context, JaxbSchemaMetadata schema, ComplexTypeExp expr, String name, QName qname, boolean abztract,
        @Nullable Class<T> beanClass, @Nullable Class<? extends T> beanImplClass) {
        super(context, schema, expr, name, qname, abztract, beanClass, beanImplClass);
    }
}
