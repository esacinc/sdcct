package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataBuilder;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;

public interface RfdResourceMetadataBuilder extends
    ResourceMetadataBuilder<RfdResourceType, IdentifiedExtensionType, RfdResourceMetadata<? extends IdentifiedExtensionType>> {
    public SearchParamMetadata[] getSearchParamMetadatas();

    public void setSearchParamMetadatas(SearchParamMetadata ... searchParamMetadatas);
}
