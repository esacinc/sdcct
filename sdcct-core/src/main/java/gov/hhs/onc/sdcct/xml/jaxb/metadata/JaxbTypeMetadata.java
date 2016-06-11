package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import java.io.Serializable;
import javax.annotation.Nullable;

public interface JaxbTypeMetadata<T extends Serializable, U> extends JaxbSchemaMetadataComponent<T> {
    public boolean hasBeanClass();

    @Nullable
    public Class<U> getBeanClass();

    public boolean hasBeanImplClass();

    @Nullable
    public Class<? extends U> getBeanImplClass();
}
