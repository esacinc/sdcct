package gov.hhs.onc.sdcct.rfd.ws.metadata.impl;

import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdInteractionWsMetadata;
import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdResourceWsMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractResourceWsMetadata;

public class RfdResourceWsMetadataImpl<T extends IdentifiedExtensionType>
    extends AbstractResourceWsMetadata<T, RfdResourceMetadata<T>, RfdInteractionWsMetadata> implements RfdResourceWsMetadata<T> {
    public RfdResourceWsMetadataImpl(RfdResourceMetadata<T> resourceMetadata) {
        super(resourceMetadata);
    }
}
