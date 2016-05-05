package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.search.SearchService;

public interface FhirResourceSearchService<T extends DomainResource> extends SearchService<T, FhirResource, FhirResourceRegistry<T>> {
}
