package gov.hhs.onc.sdcct.form.manager;

import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.io.impl.ResourceSource;
import gov.hhs.onc.sdcct.sdc.FormDesignType;
import javax.annotation.Nullable;
import javax.ws.rs.core.MultivaluedMap;

public interface FormManager extends FormService {
    @Nullable
    public FormDesignType findFormDesign(String id) throws Exception;

    public Bundle findQuestionnaires(MultivaluedMap<String, String> params) throws Exception;
    
    @Nullable
    public Questionnaire findQuestionnaire(String id) throws Exception;

    public boolean hasInternalFormDesignSources();

    @Nullable
    public ResourceSource[] getInternalFormDesignSources();

    public void setInternalFormDesignSources(ResourceSource ... internalFormDesignSrcs);

    public boolean hasInternalQuestionnaireSources();

    @Nullable
    public ResourceSource[] getInternalQuestionnaireSources();

    public void setInternalQuestionnaireSources(ResourceSource ... internalQuestionnaireSrcs);
}
