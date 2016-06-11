package gov.hhs.onc.sdcct.fhir.search;

import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;

public interface FhirSearchService<T extends Resource> extends SearchService<T, FhirResourceMetadata<T>, FhirResource, FhirResourceRegistry<T>> {
}
