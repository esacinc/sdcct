package gov.hhs.onc.sdcct.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.BundleEntry;
import gov.hhs.onc.sdcct.fhir.BundleTypeList;
import gov.hhs.onc.sdcct.fhir.FhirFormRegistry;
import gov.hhs.onc.sdcct.fhir.FhirFormSearchService;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.BundleEntryImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleImpl;
import gov.hhs.onc.sdcct.fhir.impl.BundleTypeImpl;
import gov.hhs.onc.sdcct.fhir.impl.QuestionnaireImpl;
import gov.hhs.onc.sdcct.fhir.impl.ResourceContainerImpl;
import gov.hhs.onc.sdcct.fhir.impl.UnsignedIntImpl;
import gov.hhs.onc.sdcct.form.impl.AbstractFormService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.rfd.RfdFormRegistry;
import gov.hhs.onc.sdcct.rfd.RfdFormSearchService;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import gov.hhs.onc.sdcct.sdc.impl.FormDesignTypeImpl;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Nullable;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class FormManagerImpl extends AbstractFormService implements FormManager {
    @Autowired
    private FhirFormRegistry fhirFormRegistry;

    @Autowired
    private FhirFormSearchService fhirFormSearchService;

    @Autowired
    private RfdFormRegistry rfdFormRegistry;

    @Autowired
    private RfdFormSearchService rfdFormSearchService;

    @Autowired
    private XmlCodec xmlCodec;

    private ResourceSource[] internalFormDesignSrcs;
    private ResourceSource[] internalQuestionnaireSrcs;

    @Nullable
    @Override
    public FormDesignType findFormDesign(String id) throws Exception {
        return this.rfdFormRegistry.findByNaturalId(id);
    }

    @Override
    public Bundle findQuestionnaires(MultivaluedMap<String, String> params) throws Exception {
        List<Questionnaire> questionnaires = this.fhirFormSearchService.search(params);
        Bundle bundle =
            new BundleImpl().setTotal(new UnsignedIntImpl().setValue(BigInteger.valueOf(questionnaires.size()))).setType(
                new BundleTypeImpl().setValue(BundleTypeList.SEARCHSET));

        questionnaires.stream().forEach(
            questionnaire -> bundle.addEntry(((BundleEntry) new BundleEntryImpl().setId(questionnaire.getId().getValue()))
                .setResource(new ResourceContainerImpl().setContent(questionnaire))));

        return bundle;
    }

    @Nullable
    @Override
    public Questionnaire findQuestionnaire(String id) throws Exception {
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
