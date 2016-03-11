package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.search.SearchService;

public interface FhirFormResponseSearchService extends
    SearchService<QuestionnaireResponse, FhirFormResponse, FhirFormResponseDao, FhirFormResponseDataService, FhirFormResponseRegistry> {
}
