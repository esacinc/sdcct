package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.ws.FhirWebServiceException;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.ws.impl.AbstractFhirFormWebService;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.web.form.manager.FhirFormManagerWebService;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;

@Component("wsFormManagerFhirImpl")
public class FhirFormManagerWebServiceImpl extends AbstractFhirFormWebService<FormManager> implements FhirFormManagerWebService {
    @Override
    public Questionnaire readQuestionnaire(String questionnaireId) throws Exception {
        try {
            Questionnaire questionnaire = this.service.findQuestionnaireById(questionnaireId);

            if (questionnaire == null) {
                throw new FhirWebServiceException(String.format("Questionnaire (id=%s) is unavailable.", questionnaireId))
                    .setIssueCodeType(IssueCodeType.NOT_FOUND).setIssueDetailConceptParts("MSG_NO_EXIST", questionnaireId).setResponseStatus(Status.NOT_FOUND);
            }

            return questionnaire;
        } catch (FhirWebServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirWebServiceException(String.format("Unable to retrieve questionnaire (id=%s)", questionnaireId))
                .setIssueCodeType(IssueCodeType.PROCESSING);
        }
    }
}
