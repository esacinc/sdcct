package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import com.sun.msv.grammar.xmlschema.XMLSchemaTypeExp;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import javax.annotation.Nullable;
import javax.xml.namespace.QName;

public abstract class AbstractJaxbTypeMetadata<T extends XMLSchemaTypeExp, U> extends AbstractJaxbSchemaMetadataComponent<T> implements JaxbTypeMetadata<T, U> {
    protected Class<U> beanClass;
    protected Class<? extends U> beanImplClass;

    protected AbstractJaxbTypeMetadata(JaxbContextMetadata context, JaxbSchemaMetadata schema, T expr, String name, QName qname, boolean abztract,
        @Nullable Class<U> beanClass, @Nullable Class<? extends U> beanImplClass) {
        super(context, schema, expr, name, qname, abztract);

        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public boolean hasBeanClass() {
        return (this.beanClass != null);
    }

    @Nullable
    @Override
    public Class<U> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public boolean hasBeanImplClass() {
        return (this.beanImplClass != null);
    }

    @Nullable
    @Override
    public Class<? extends U> getBeanImplClass() {
        return this.beanImplClass;
    }
}
