package gov.hhs.onc.sdcct.rfd.ws.metadata;

import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;

public interface RfdResourceWsMetadata<T extends IdentifiedExtensionType> extends ResourceWsMetadata<T, RfdResourceMetadata<T>, RfdInteractionWsMetadata> {
}
