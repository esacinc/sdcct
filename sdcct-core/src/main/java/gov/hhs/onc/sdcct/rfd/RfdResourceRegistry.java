package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceRegistry<T extends IdentifiedExtensionType> extends SdcctResourceRegistry<T, RfdResourceMetadata<T>, RfdResource> {
}
