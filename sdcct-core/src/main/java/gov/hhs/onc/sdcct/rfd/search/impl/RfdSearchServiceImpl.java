package gov.hhs.onc.sdcct.rfd.search.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.rfd.RfdResource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.metadata.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.search.RfdSearchService;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import java.net.URI;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public class RfdSearchServiceImpl<T extends IdentifiedExtensionType>
    extends AbstractSearchService<T, RfdResourceMetadata<T>, RfdResource, RfdResourceRegistry<T>> implements RfdSearchService<T> {
    public RfdSearchServiceImpl(RfdResourceMetadata<T> resourceMetadata, RfdResourceRegistry<T> resourceRegistry) {
        super(resourceMetadata, resourceRegistry);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> search(MultivaluedMap<String, String> params) throws Exception {
        // TEMP: placeholder
        if (!params.containsKey(ResourceParamNames.IDENTIFIER)) {
            return super.search(params);
        }

        return this.resourceRegistry.findAllBeans(this.resourceRegistry.buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.URI_PARAMS,
            ResourceParamNames.IDENTIFIER, URI.create(params.getFirst(ResourceParamNames.IDENTIFIER)))));
    }
}
