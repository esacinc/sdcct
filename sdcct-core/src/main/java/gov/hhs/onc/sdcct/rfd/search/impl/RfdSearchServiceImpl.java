package gov.hhs.onc.sdcct.rfd.search.impl;

import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.search.RfdSearchService;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public class RfdSearchServiceImpl<T extends IdentifiedExtensionType>
    extends AbstractSearchService<T, RfdResourceMetadata<T>, RfdResource, RfdResourceRegistry<T>> implements RfdSearchService<T> {
    public RfdSearchServiceImpl(RfdResourceMetadata<T> resourceMetadata, RfdResourceRegistry<T> resourceRegistry) {
        super(resourceMetadata, resourceRegistry);
    }
}
