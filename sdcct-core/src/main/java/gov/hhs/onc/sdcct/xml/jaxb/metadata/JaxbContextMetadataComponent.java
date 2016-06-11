package gov.hhs.onc.sdcct.xml.jaxb.metadata;

import java.io.Serializable;

public interface JaxbContextMetadataComponent<T extends Serializable> extends JaxbMetadataComponent {
    public JaxbContextMetadata getContext();

    public T getExpression();
}
