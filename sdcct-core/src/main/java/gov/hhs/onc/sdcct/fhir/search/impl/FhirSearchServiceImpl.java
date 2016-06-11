package gov.hhs.onc.sdcct.fhir.search.impl;

import gov.hhs.onc.sdcct.data.search.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.search.FhirSearchService;

public class FhirSearchServiceImpl<T extends Resource> extends AbstractSearchService<T, FhirResourceMetadata<T>, FhirResource, FhirResourceRegistry<T>>
    implements FhirSearchService<T> {
    public FhirSearchServiceImpl(FhirResourceMetadata<T> resourceMetadata, FhirResourceRegistry<T> resourceRegistry) {
        super(resourceMetadata, resourceRegistry);
    }
}
