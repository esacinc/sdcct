package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.Bundle;
import gov.hhs.onc.sdcct.fhir.FhirFormException;
import gov.hhs.onc.sdcct.fhir.IssueType;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeType;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.ws.impl.AbstractFhirFormWebService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.web.form.manager.FhirFormManagerWebService;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;

@Component("wsFormManagerFhirImpl")
public class FhirFormManagerWebServiceImpl extends AbstractFhirFormWebService<FormManager> implements FhirFormManagerWebService {
    @Override
    public Bundle findQuestionnaires(MultivaluedMap<String, String> formParams) throws Exception {
        return this.service.findQuestionnaires(formParams);
    }

    @Override
    public Bundle findQuestionnaires() throws Exception {
        return this.service.findQuestionnaires(this.msgContext.getUriInfo().getQueryParameters());
    }

    @Override
    public Questionnaire findQuestionnaire(String questionnaireId) throws Exception {
        try {
            Questionnaire questionnaire = this.service.findQuestionnaire(questionnaireId);

            if (questionnaire == null) {
                throw new FhirFormException(String.format("Questionnaire (id=%s) is unavailable.", questionnaireId)).setIssueType(IssueType.NOT_FOUND)
                    .setOperationOutcomeArgs(questionnaireId).setOperationOutcomeType(OperationOutcomeType.MSG_NO_EXIST).setResponseStatus(Status.NOT_FOUND);
            }

            return questionnaire;
        } catch (FhirFormException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirFormException(String.format("Unable to retrieve questionnaire (id=%s)", questionnaireId)).setIssueType(IssueType.NOT_FOUND);
        }
    }
}
