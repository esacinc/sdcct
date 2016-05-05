package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceMetadataImpl<T extends IdentifiedExtensionType> extends AbstractResourceMetadata<RfdResourceType, T> implements RfdResourceMetadata<T> {
    public RfdResourceMetadataImpl(String id, String name, RfdResourceType type, Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(SpecificationType.RFD, id, name, null, type, beanClass, beanImplClass);
    }
}
