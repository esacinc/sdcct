package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormResponse;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDao;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseDataService;
import gov.hhs.onc.sdcct.fhir.FhirFormResponseRegistry;
import gov.hhs.onc.sdcct.fhir.QuestionnaireResponse;
import org.springframework.stereotype.Component;

@Component("registryFormRespFhir")
public class FhirFormResponseRegistryImpl extends
    AbstractFhirResourceRegistry<QuestionnaireResponse, FhirFormResponse, FhirFormResponseDao, FhirFormResponseDataService> implements FhirFormResponseRegistry {
    public FhirFormResponseRegistryImpl() {
        super(QuestionnaireResponse.class, QuestionnaireResponseImpl.class, FhirFormResponse.class, FhirFormResponseImpl.class, FhirFormResponseImpl::new);
    }

    @Override
    protected FhirFormResponse buildSearchParams(QuestionnaireResponse bean, FhirFormResponse entity) throws Exception {
        super.buildSearchParams(bean, entity);

        
        
        return entity;
    }

    @Override
    protected FhirFormResponse encode(QuestionnaireResponse bean) throws Exception {
        FhirFormResponse entity = super.encode(bean);
        entity.setId(bean.getId().getValue());

        return entity;
    }
}
