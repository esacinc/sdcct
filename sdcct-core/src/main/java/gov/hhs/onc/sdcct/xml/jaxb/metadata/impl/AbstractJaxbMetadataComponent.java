package gov.hhs.onc.sdcct.xml.jaxb.metadata.impl;

import gov.hhs.onc.sdcct.metadata.impl.AbstractMetadataComponent;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbMetadataComponent;

public abstract class AbstractJaxbMetadataComponent extends AbstractMetadataComponent implements JaxbMetadataComponent {
    protected AbstractJaxbMetadataComponent(String name) {
        super(name);
    }
}
