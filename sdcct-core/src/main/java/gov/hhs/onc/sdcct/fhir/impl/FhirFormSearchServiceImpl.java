package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.data.db.impl.AbstractSearchService;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirFormDao;
import gov.hhs.onc.sdcct.fhir.FhirFormDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormRegistry;
import gov.hhs.onc.sdcct.fhir.FhirFormSearchService;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import org.springframework.stereotype.Component;

@Component("searchServiceFormFhir")
public class FhirFormSearchServiceImpl extends AbstractSearchService<Questionnaire, FhirForm, FhirFormDao, FhirFormDataService, FhirFormRegistry> implements
    FhirFormSearchService {
    public FhirFormSearchServiceImpl() {
        super(Questionnaire.class, QuestionnaireImpl.class, FhirForm.class, FhirFormImpl.class);
    }
}
