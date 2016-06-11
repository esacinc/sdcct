package gov.hhs.onc.sdcct.rfd.search;

import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdSearchService<T extends IdentifiedExtensionType> extends SearchService<T, RfdResourceMetadata<T>, RfdResource, RfdResourceRegistry<T>> {
}
