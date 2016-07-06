package gov.hhs.onc.sdcct.rfd.metadata.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadata;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceMetadataImpl<T extends IdentifiedExtensionType> extends AbstractResourceMetadata<T> implements RfdResourceMetadata<T> {
    public RfdResourceMetadataImpl(String id, String name, String path, Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(SpecificationType.RFD, id, name, path, beanClass, beanImplClass);
    }
}
