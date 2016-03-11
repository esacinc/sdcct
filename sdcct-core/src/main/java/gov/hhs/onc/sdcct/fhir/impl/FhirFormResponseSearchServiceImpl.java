package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.fhir.FhirFormResponse;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDao;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseRegistry;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseSearchService;
import gov.hhs.onc.sdcct.fhir.QuestionnaireResponse;
import org.springframework.stereotype.Component;

@Component("searchServiceFormRespFhir")
public class FhirFormResponseSearchServiceImpl extends
    AbstractSearchService<QuestionnaireResponse, FhirFormResponse, FhirFormResponseDao, FhirFormResponseDataService, FhirFormResponseRegistry> implements
    FhirFormResponseSearchService {
    public FhirFormResponseSearchServiceImpl() {
        super(QuestionnaireResponse.class, QuestionnaireResponseImpl.class, FhirFormResponse.class, FhirFormResponseImpl.class);
    }
}
