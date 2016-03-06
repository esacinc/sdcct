package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.search.SearchService;

public interface FhirFormSearchService extends SearchService<Questionnaire, FhirForm, FhirFormDao, FhirFormDataService, FhirFormRegistry> {
}
