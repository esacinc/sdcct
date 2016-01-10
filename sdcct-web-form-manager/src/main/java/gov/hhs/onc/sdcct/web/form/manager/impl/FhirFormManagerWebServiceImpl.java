package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.AbstractFhirFormWebService;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.web.form.manager.FhirFormManagerWebService;
import javax.ws.rs.WebApplicationException;
import org.springframework.stereotype.Component;

@Component("wsFormManagerFhirImpl")
public class FhirFormManagerWebServiceImpl extends AbstractFhirFormWebService<FormManager> implements FhirFormManagerWebService {
    @Override
    public Questionnaire readQuestionnaire(String questionnaireId) throws Exception {
        try {
            Form form = this.service.retrieveForm(questionnaireId);

            if (form == null) {
                throw new WebApplicationException(String.format("Form (id=%s) is unavailable.", questionnaireId));
            }

            if (!form.isSetQuestionnaire()) {
                throw new WebApplicationException(String.format("FHIR SDC variant of the specified form (id=%s) is unavailable.", questionnaireId));
            }

            return form.getQuestionnaire();
        } catch (Exception e) {
            throw new WebApplicationException(String.format("Unable to retrieve form (id=%s)", questionnaireId), e);
        }
    }
}
