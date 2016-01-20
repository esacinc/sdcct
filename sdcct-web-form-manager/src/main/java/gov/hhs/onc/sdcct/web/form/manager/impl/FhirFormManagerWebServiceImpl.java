package gov.hhs.onc.sdcct.web.form.manager.impl;

import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.fhir.Questionnaire;
import gov.hhs.onc.sdcct.fhir.impl.AbstractFhirFormWebService;
import gov.hhs.onc.sdcct.form.Form;
import gov.hhs.onc.sdcct.form.manager.FormManager;
import gov.hhs.onc.sdcct.web.form.manager.FhirFormManagerWebService;
import javax.ws.rs.core.Response.Status;
import org.springframework.stereotype.Component;

@Component("wsFormManagerFhirImpl")
public class FhirFormManagerWebServiceImpl extends AbstractFhirFormWebService<FormManager> implements FhirFormManagerWebService {
    @Override
    public Questionnaire readQuestionnaire(String questionnaireId) throws Exception {
        try {
            Form form = this.service.retrieveForm(questionnaireId);

            if (form == null) {
                throw new FhirException(String.format("Form (id=%s) is unavailable.", questionnaireId)).setIssueCodeType(IssueCodeType.NOT_FOUND)
                    .setIssueDetailConceptParts("MSG_NO_EXIST", questionnaireId).setRespStatus(Status.NOT_FOUND);
            }

            if (!form.isSetQuestionnaire()) {
                throw new FhirException(String.format("FHIR SDC variant of the specified form (id=%s) is unavailable.", questionnaireId))
                    .setIssueCodeType(IssueCodeType.NOT_FOUND).setIssueDetailConceptParts("MSG_NO_EXIST", questionnaireId).setRespStatus(Status.NOT_FOUND);
            }

            return form.getQuestionnaire();
        } catch (FhirException e) {
            throw e;
        } catch (Exception e) {
            throw new FhirException(String.format("Unable to retrieve form (id=%s)", questionnaireId)).setIssueCodeType(IssueCodeType.PROCESSING);
        }
    }
}
