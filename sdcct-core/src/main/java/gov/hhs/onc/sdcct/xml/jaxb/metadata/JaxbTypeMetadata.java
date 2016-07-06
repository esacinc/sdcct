package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import com.sun.msv.grammar.xmlschema.XMLSchemaTypeExp;
import javax.annotation.Nullable;

public interface JaxbTypeMetadata<T extends XMLSchemaTypeExp, U> extends JaxbSchemaMetadataComponent<T> {
    public boolean hasBeanClass();

    @Nullable
    public Class<U> getBeanClass();

    public boolean hasBeanImplClass();

    @Nullable
    public Class<? extends U> getBeanImplClass();
}
