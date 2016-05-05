package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceMetadata<T extends IdentifiedExtensionType> extends ResourceMetadata<RfdResourceType, T> {
}
