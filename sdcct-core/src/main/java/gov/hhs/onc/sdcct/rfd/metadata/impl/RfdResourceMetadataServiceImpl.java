package gov.hhs.onc.sdcct.rfd.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadataService;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadataService;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdResourceMetadataServiceImpl extends AbstractResourceMetadataService<IdentifiedExtensionType, RfdResourceMetadata<?>>
    implements RfdResourceMetadataService {
    public RfdResourceMetadataServiceImpl() {
        super(SpecificationType.RFD);
    }
}
