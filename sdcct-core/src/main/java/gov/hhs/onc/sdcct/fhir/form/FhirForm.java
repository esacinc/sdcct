package gov.hhs.onc.sdcct.fhir.form;

import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.SdcctForm;

public interface FhirForm extends SdcctForm<Questionnaire> {
    @Override
    public FhirForm build() throws Exception;
}
