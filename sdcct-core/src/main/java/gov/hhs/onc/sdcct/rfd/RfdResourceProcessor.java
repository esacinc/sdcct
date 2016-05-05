package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.ResourceProcessor;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceProcessor extends
    ResourceProcessor<RfdResourceType, IdentifiedExtensionType, RfdResource, RfdResourceMetadata<? extends IdentifiedExtensionType>> {
}
