package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceRegistry<T extends IdentifiedExtensionType> extends SdcctRegistry<T, RfdResource> {
}
