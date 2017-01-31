package gov.hhs.onc.sdcct.fhir.search.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.search.FhirSearchService;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public class FhirSearchServiceImpl<T extends Resource> extends AbstractSearchService<T, FhirResourceMetadata<T>, FhirResource, FhirResourceRegistry<T>>
    implements FhirSearchService<T> {
    public FhirSearchServiceImpl(FhirResourceMetadata<T> resourceMetadata, FhirResourceRegistry<T> resourceRegistry) {
        super(resourceMetadata, resourceRegistry);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> search(MultivaluedMap<String, String> params) throws Exception {
        // TEMP: placeholder
        if (!params.containsKey(ResourceParamNames.IDENTIFIER)) {
            return super.search(params);
        }

        return this.resourceRegistry.findAllBeans(this.resourceRegistry.buildCriteria(
            SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, params.getFirst(ResourceParamNames.IDENTIFIER))));
    }
}
