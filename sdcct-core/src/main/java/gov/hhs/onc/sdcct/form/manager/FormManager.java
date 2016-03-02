package gov.hhs.onc.sdcct.form.manager;

import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import javax.annotation.Nullable;

public interface FormManager extends FormService {
    @Nullable
    public FormDesignType findFormDesignById(String id) throws Exception;

    @Nullable
    public Questionnaire findQuestionnaireById(String id) throws Exception;

    public boolean hasInternalFormDesignSources();

    @Nullable
    public ResourceSource[] getInternalFormDesignSources();

    public void setInternalFormDesignSources(ResourceSource ... internalFormDesignSrcs);

    public boolean hasInternalQuestionnaireSources();

    @Nullable
    public ResourceSource[] getInternalQuestionnaireSources();

    public void setInternalQuestionnaireSources(ResourceSource ... internalQuestionnaireSrcs);
}
