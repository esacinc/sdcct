package gov.hhs.onc.sdcct.form.manager.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.BundleType;
import gov.hhs.onc.sdcct.fhir.FhirResourceRegistry;
import gov.hhs.onc.sdcct.fhir.FhirResourceSearchService;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.BundleEntryImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleTypeComponentImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceContainerImpl;
import gov.hhs.onc.sdcct.fhir.impl.UnsignedIntTypeImpl;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.rfd.RfdResourceRegistry;
import gov.hhs.onc.sdcct.rfd.RfdResourceSearchService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class FormManagerImpl extends AbstractFormService implements FormManager {
    @Resource(name = "registryResourceFhirQuestionnaire")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private FhirResourceRegistry<Questionnaire> questionnaireRegistry;

    @Resource(name = "searchServiceResourceFhirQuestionnaire")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private FhirResourceSearchService<Questionnaire> questionnaireSearchService;

    @Resource(name = "registryResourceRfdFormDesign")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private RfdResourceRegistry<FormDesignType> formDesignRegistry;

    @Resource(name = "searchServiceResourceRfdFormDesign")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private RfdResourceSearchService<FormDesignType> formDesignSearchService;

    @Autowired
    private XmlCodec xmlCodec;

    private ResourceSource[] internalFormDesignSrcs;
    private ResourceSource[] internalQuestionnaireSrcs;

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public FormDesignType findFormDesign(String id) throws Exception {
        return this.formDesignRegistry.find(this.formDesignRegistry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
            root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), id)));
    }

    @Override
    public Bundle findQuestionnaires(MultivaluedMap<String, String> params) throws Exception {
        List<Questionnaire> questionnaires = this.questionnaireSearchService.search(params);
        Bundle bundle =
            new BundleImpl().setTotal(new UnsignedIntTypeImpl().setValue(BigInteger.valueOf(questionnaires.size()))).setType(
                new BundleTypeComponentImpl().setValue(BundleType.SEARCHSET));

        questionnaires.stream().forEach(
            questionnaire -> bundle.addEntry(new BundleEntryImpl().setId(questionnaire.getId().getValue()).setResource(
                new ResourceContainerImpl().setContent(questionnaire))));

        return bundle;
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public Questionnaire findQuestionnaire(String id) throws Exception {
        return this.questionnaireRegistry.find(this.questionnaireRegistry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root
            .join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), id)));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() throws Exception {
        if (this.hasInternalQuestionnaireSources()) {
            Questionnaire internalQuestionnaire;

            for (ResourceSource internalQuestionnaireSrc : this.internalQuestionnaireSrcs) {
                final String internalIdentifier =
                    (internalQuestionnaire = this.xmlCodec.decode(internalQuestionnaireSrc, QuestionnaireImpl.class, null)).getIdentifiers().get(0).getValue()
                        .getValue();

                if (!this.questionnaireRegistry.exists(this.questionnaireRegistry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                    .equal(root.join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), internalIdentifier)))) {
                    this.questionnaireRegistry.save(internalQuestionnaire);
                }
            }
        }

        if (this.hasInternalFormDesignSources()) {
            FormDesignType internalFormDesign;

            for (ResourceSource internalFormDesignSrc : this.internalFormDesignSrcs) {
                final String internalIdentifier = (internalFormDesign = this.xmlCodec.decode(internalFormDesignSrc, FormDesignTypeImpl.class, null)).getId();

                if (!this.formDesignRegistry.exists(this.formDesignRegistry.buildCriteria((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root
                    .join(DbPropertyNames.TOKEN_SEARCH_PARAMS).get(DbPropertyNames.VALUE), internalIdentifier)))) {
                    this.formDesignRegistry.save(internalFormDesign);
                }
            }
        }
    }

    @Override
    public boolean hasInternalFormDesignSources() {
        return !ArrayUtils.isEmpty(this.internalFormDesignSrcs);
    }

    @Nullable
    @Override
    public ResourceSource[] getInternalFormDesignSources() {
        return this.internalFormDesignSrcs;
    }

    @Override
    public void setInternalFormDesignSources(ResourceSource ... internalFormDesignSrcs) {
        this.internalFormDesignSrcs = internalFormDesignSrcs;
    }

    @Override
    public boolean hasInternalQuestionnaireSources() {
        return !ArrayUtils.isEmpty(this.internalQuestionnaireSrcs);
    }

    @Nullable
    @Override
    public ResourceSource[] getInternalQuestionnaireSources() {
        return this.internalQuestionnaireSrcs;
    }

    @Override
    public void setInternalQuestionnaireSources(ResourceSource ... internalQuestionnaireSrcs) {
        this.internalQuestionnaireSrcs = internalQuestionnaireSrcs;
    }
}
