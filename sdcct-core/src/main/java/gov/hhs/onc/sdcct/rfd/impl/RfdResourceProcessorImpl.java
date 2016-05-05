package gov.hhs.onc.sdcct.rfd.impl;

import gov.hhs.onc.sdcct.data.impl.AbstractResourceProcessor;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceProcessor;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceProcessorImpl extends
    AbstractResourceProcessor<RfdResourceType, IdentifiedExtensionType, RfdResource, RfdResourceMetadata<? extends IdentifiedExtensionType>> implements
    RfdResourceProcessor {
    public RfdResourceProcessorImpl() {
        super(IdentifiedExtensionType.class, RfdResource.class, MetadataService::getRfdResourceMetadatas);
    }
}
