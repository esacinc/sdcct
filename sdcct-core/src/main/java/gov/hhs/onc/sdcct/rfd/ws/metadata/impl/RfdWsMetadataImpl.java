package gov.hhs.onc.sdcct.rfd.ws.metadata.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdInteractionWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdResourceWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractWsMetadata;

public class RfdWsMetadataImpl extends AbstractWsMetadata<RfdInteractionWsMetadata, RfdResourceWsMetadata<?>> implements RfdWsMetadata {
    public RfdWsMetadataImpl(String id, String name) {
        super(SpecificationType.RFD, id, name);
    }
}
