package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.CodeableConcept;
import gov.hhs.onc.sdcct.fhir.Coding;
import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.FhirVocabRepository;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.fhir.IssueSeverityType;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import gov.hhs.onc.sdcct.ws.WsPropertyNames;
import java.util.Collections;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("exceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    private final static String OP_OUTCOME_VALUE_SET_ID = "operation-outcome";

    private final static Logger LOGGER = LoggerFactory.getLogger(FhirExceptionMapper.class);

    @Autowired
    private FhirVocabRepository vocabRepo;

    @Override
    public Response toResponse(Throwable exception) {
        IssueCodeType issueCodeType = IssueCodeType.EXCEPTION;
        CodeableConcept issueDetailConcept = null;
        Status respStatus = Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof FhirException) {
            FhirException fhirException = ((FhirException) exception);
            issueCodeType = fhirException.getIssueCodeType();
            respStatus = fhirException.getResponseStatus();

            if (fhirException.hasIssueDetailConceptParts()) {
                Pair<String, Object[]> issueDetailConceptParts = fhirException.getIssueDetailConceptParts();
                String issueDetailConceptCode = issueDetailConceptParts.getKey();
                CodeableConcept baseIssueDetailConcept = this.vocabRepo.getValueSetConcepts().get(OP_OUTCOME_VALUE_SET_ID, issueDetailConceptCode);

                if (baseIssueDetailConcept != null) {
                    Coding baseIssueDetailConceptCoding = baseIssueDetailConcept.getCoding().get(0);

                    issueDetailConcept =
                        new CodeableConceptImpl().setCoding(Collections.singletonList(new CodingImpl().setCode(baseIssueDetailConceptCoding.getCode())
                            .setDisplay(new StringValueImpl()
                                .setValue(String.format(baseIssueDetailConceptCoding.getDisplay().getValue(), issueDetailConceptParts.getValue())))
                        .setSystem(baseIssueDetailConceptCoding.getSystem()).setVersion(baseIssueDetailConceptCoding.getVersion())));
                } else {
                    LOGGER.error(String.format("Unknown FHIR operation outcome issue detail concept (code=%s).", issueDetailConceptCode));
                }
            }
        }

        OperationOutcomeIssue opOutcomeIssue = new OperationOutcomeIssueImpl().setCode(new IssueCodeImpl().setValue(issueCodeType))
            .setDetails(issueDetailConcept).setSeverity(new IssueSeverityImpl().setValue(IssueSeverityType.ERROR));

        if (MessageUtils.getContextualBoolean(JAXRSUtils.getCurrentMessage(), WsPropertyNames.ERROR_STACK_TRACE, false)) {
            opOutcomeIssue.setDiagnostics(new StringValueImpl().setValue(SdcctExceptionUtils.buildRootCauseStackTrace(exception)));
        }

        return Response.status(respStatus).entity(new OperationOutcomeImpl().setIssue(Collections.singletonList(opOutcomeIssue))).build();
    }
}
