package gov.hhs.onc.sdcct.form.manager.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamNames;
import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.BundleType;
import gov.hhs.onc.sdcct.fhir.FhirForm;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.BundleEntryImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleTypeComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceContainerImpl;
import gov.hhs.onc.sdcct.fhir.impl.UnsignedIntTypeImpl;
import gov.hhs.onc.sdcct.fhir.search.FhirSearchService;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.rfd.RfdForm;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.search.RfdSearchService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.beans.factory.annotation.Autowired;

public class FormManagerImpl extends AbstractFormService implements FormManager {
    @Resource(name = "resourceRegistryFhirQuestionnaire")
    private FhirResourceRegistry<Questionnaire> questionnaireRegistry;

    @Resource(name = "searchServiceFhirQuestionnaire")
    private FhirSearchService<Questionnaire> questionnaireSearchService;

    @Resource(name = "resourceRegistryRfdFormDesign")
    private RfdResourceRegistry<FormDesignType> formDesignRegistry;

    @Resource(name = "searchServiceRfdFormDesign")
    private RfdSearchService<FormDesignType> formDesignSearchService;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<FhirForm> fhirForms;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<RfdForm> rfdForms;

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public FormDesignType findFormDesign(String identifier) throws Exception {
        return this.formDesignRegistry.findBean(this.formDesignRegistry
            .buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.URI_PARAMS, ResourceParamNames.IDENTIFIER, URI.create(identifier))));
    }

    @Override
    public Bundle findQuestionnaires(MultivaluedMap<String, String> params) throws Exception {
        List<Questionnaire> questionnaires = this.questionnaireSearchService.search(params);
        Bundle bundle = new BundleImpl().setTotal(new UnsignedIntTypeImpl().setValue(BigInteger.valueOf(questionnaires.size())))
            .setType(new BundleTypeComponentImpl().setValue(BundleType.SEARCHSET));

        questionnaires.stream().forEach(questionnaire -> bundle
            .addEntry(new BundleEntryImpl().setId(questionnaire.getId().getValue()).setResource(new ResourceContainerImpl().setContent(questionnaire))));

        return bundle;
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public Questionnaire findQuestionnaire(String id) throws Exception {
        return this.questionnaireRegistry.findBean(this.questionnaireRegistry.buildCriteria(SdcctCriterionUtils.matchId(Long.parseLong(id))));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() throws Exception {
        for (FhirForm fhirForm : this.fhirForms) {
            if (!this.questionnaireRegistry.exists(this.questionnaireRegistry
                .buildCriteria(SdcctCriterionUtils.matchParam(DbPropertyNames.TOKEN_PARAMS, ResourceParamNames.IDENTIFIER, fhirForm.getIdentifier())))) {
                this.questionnaireRegistry.saveBean(fhirForm.getBean());
            }
        }

        for (RfdForm rfdForm : this.rfdForms) {
            if (!this.formDesignRegistry.exists(this.formDesignRegistry.buildCriteria(
                SdcctCriterionUtils.matchParam(DbPropertyNames.URI_PARAMS, ResourceParamNames.IDENTIFIER, URI.create(rfdForm.getIdentifier()))))) {
                this.formDesignRegistry.saveBean(rfdForm.getBean());
            }
        }
    }
}
