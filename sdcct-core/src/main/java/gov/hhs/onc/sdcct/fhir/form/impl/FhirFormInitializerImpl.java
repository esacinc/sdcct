package gov.hhs.onc.sdcct.fhir.form.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.fhir.FhirResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.form.FhirForm;
import gov.hhs.onc.sdcct.fhir.form.FhirFormInitializer;
import gov.hhs.onc.sdcct.form.impl.AbstractFormInitializer;

public class FhirFormInitializerImpl extends AbstractFormInitializer<Questionnaire, FhirResource, FhirResourceRegistry<Questionnaire>, FhirForm>
    implements FhirFormInitializer {
    public FhirFormInitializerImpl(FhirResourceRegistry<Questionnaire> registry) {
        super(registry);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected SdcctCriteria<FhirResource> buildCriteria(FhirForm form) {
        return this.registry.buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, form.getIdentifier()));
    }
}
