package gov.hhs.onc.sdcct.rfd.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.data.metadata.impl.AbstractResourceMetadataBuilder;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadataBuilder;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import gov.hhs.onc.sdcct.sdc.impl.AbstractIdentifiedExtensionType;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

public class RfdResourceMetadataBuilderImpl extends
    AbstractResourceMetadataBuilder<RfdResourceType, IdentifiedExtensionType, RfdResourceMetadata<? extends IdentifiedExtensionType>> implements
    RfdResourceMetadataBuilder {
    private SearchParamMetadata[] searchParamMetadatas;

    public RfdResourceMetadataBuilderImpl() {
        super(SpecificationType.RFD);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected Map<RfdResourceType, RfdResourceMetadata<? extends IdentifiedExtensionType>> buildInternal() throws Exception {
        Map<RfdResourceType, RfdResourceMetadata<? extends IdentifiedExtensionType>> resources = new EnumMap<>(RfdResourceType.class);
        String resourceId;
        Package resourcePkg = IdentifiedExtensionType.class.getPackage();
        Class<IdentifiedExtensionType> resourceClass;
        RfdResourceMetadata<?> resourceMetadata;

        for (RfdResourceType resourceType : RfdResourceType.values()) {
            resources.put(
                resourceType,
                (resourceMetadata =
                    new RfdResourceMetadataImpl<>((resourceId = resourceType.getId()), resourceType.getName(), resourceType, (resourceClass =
                        ((Class<IdentifiedExtensionType>) SdcctClassUtils.buildInterfaceClass(this.beanClassLoader, IdentifiedExtensionType.class, resourcePkg,
                            resourceId))), SdcctClassUtils.buildImplClass(this.beanClassLoader, AbstractIdentifiedExtensionType.class, resourceClass))));

            if (!ArrayUtils.isEmpty(this.searchParamMetadatas)) {
                resourceMetadata.addSearchParamMetadatas(this.searchParamMetadatas);
            }
        }

        return resources;
    }

    @Override
    public SearchParamMetadata[] getSearchParamMetadatas() {
        return this.searchParamMetadatas;
    }

    @Override
    public void setSearchParamMetadatas(SearchParamMetadata ... searchParamMetadatas) {
        this.searchParamMetadatas = searchParamMetadatas;
    }
}
