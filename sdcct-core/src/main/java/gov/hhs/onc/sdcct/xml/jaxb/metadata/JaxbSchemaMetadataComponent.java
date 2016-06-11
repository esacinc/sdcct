package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import java.io.Serializable;
import javax.xml.namespace.QName;

public interface JaxbSchemaMetadataComponent<T extends Serializable> extends JaxbContextMetadataComponent<T> {
    public boolean isAbstract();

    public QName getQname();

    public JaxbSchemaMetadata getSchema();
}
