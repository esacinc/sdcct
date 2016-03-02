package gov.hhs.onc.sdcct.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormRegistry;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.rfd.RfdFormRegistry;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class FormManagerImpl extends AbstractFormService implements FormManager {
    @Autowired
    private FhirFormRegistry fhirFormRegistry;

    @Autowired
    private RfdFormRegistry rfdFormRegistry;

    @Autowired
    private XmlCodec xmlCodec;

    private ResourceSource[] internalFormDesignSrcs;
    private ResourceSource[] internalQuestionnaireSrcs;

    @Nullable
    @Override
    public FormDesignType findFormDesignById(String id) throws Exception {
        return this.rfdFormRegistry.findByNaturalId(id);
    }

    @Nullable
    @Override
    public Questionnaire findQuestionnaireById(String id) throws Exception {
        return this.fhirFormRegistry.findByNaturalId(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.hasInternalQuestionnaireSources()) {
            Questionnaire internalQuestionnaire;

            for (ResourceSource internalQuestionnaireSrc : this.internalQuestionnaireSrcs) {
                if (!this.fhirFormRegistry.existsByNaturalId((internalQuestionnaire =
                    this.xmlCodec.decode(internalQuestionnaireSrc, QuestionnaireImpl.class, null)).getId().getValue())) {
                    this.fhirFormRegistry.save(internalQuestionnaire);
                }
            }
        }

        if (this.hasInternalFormDesignSources()) {
            FormDesignType internalFormDesign;

            for (ResourceSource internalFormDesignSrc : this.internalFormDesignSrcs) {
                if (!this.rfdFormRegistry.existsByNaturalId((internalFormDesign = this.xmlCodec.decode(internalFormDesignSrc, FormDesignTypeImpl.class, null))
                    .getFormID())) {
                    this.rfdFormRegistry.save(internalFormDesign);
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
