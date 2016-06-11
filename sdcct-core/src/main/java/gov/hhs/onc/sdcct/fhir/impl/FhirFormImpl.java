package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.impl.AbstractSdcctForm;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;

public class FhirFormImpl extends AbstractSdcctForm<Questionnaire> implements FhirForm {
    public FhirFormImpl(String name, XdmDocument doc) {
        super(SpecificationType.FHIR, Questionnaire.class, QuestionnaireImpl.class, name, doc);
    }

    @Override
    public String getIdentifier() {
        return this.bean.getIdentifiers().get(0).getValue().getValue();
    }
}
