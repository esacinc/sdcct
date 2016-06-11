package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.sun.msv.grammar.xmlschema.SimpleTypeExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSimpleTypeMetadata;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public class JaxbSimpleTypeMetadataImpl<T> extends AbstractJaxbTypeMetadata<SimpleTypeExp, T> implements JaxbSimpleTypeMetadata<T> {
    public JaxbSimpleTypeMetadataImpl(JaxbContextMetadata context, JaxbSchemaMetadata schema, SimpleTypeExp expr, String name, QName qname,
        @Nullable Class<T> beanClass, @Nullable Class<? extends T> beanImplClass) {
        super(context, schema, expr, name, qname, false, beanClass, beanImplClass);
    }
}
