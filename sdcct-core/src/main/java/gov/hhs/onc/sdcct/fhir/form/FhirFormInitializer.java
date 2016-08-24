package gov.hhs.onc.sdcct.fhir.form;

import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.FormInitializer;

public interface FhirFormInitializer extends FormInitializer<Questionnaire, FhirResource, FhirResourceRegistry<Questionnaire>, FhirForm> {
}
