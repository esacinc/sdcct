package gov.hhs.onc.sdcct.fhir.form;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.form.impl.FhirFormImpl;
import gov.hhs.onc.sdcct.form.SdcctForm;

@JsonSubTypes({ @Type(FhirFormImpl.class) })
public interface FhirForm extends SdcctForm<Questionnaire> {
    @Override
    public FhirForm build() throws Exception;
}
