package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceSearchService<T extends IdentifiedExtensionType> extends SearchService<T, RfdResource, RfdResourceRegistry<T>> {
}
