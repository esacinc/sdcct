package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.event.ResourceDbEventListener;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceDbEventListener
    extends
    ResourceDbEventListener<RfdResourceType, IdentifiedExtensionType, RfdResource, RfdResourceMetadata<? extends IdentifiedExtensionType>, RfdResourceProcessor> {
}
