package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirException;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.fhir.IssueSeverityType;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.utils.SdcctExceptionUtils;
import java.util.Collections;
import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import org.springframework.stereotype.Component;

@Component("exceptionMapperFhir")
@Priority(0)
public class FhirExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        IssueCodeType issueCodeType = IssueCodeType.EXCEPTION;
        Status respStatus = Status.INTERNAL_SERVER_ERROR;

        if (exception instanceof FhirException) {
            FhirException fhirException = ((FhirException) exception);
            issueCodeType = fhirException.getIssueCodeType();
            respStatus = fhirException.getResponseStatus();
        }

        OperationOutcomeIssue opOutcomeIssue =
            new OperationOutcomeIssueImpl().setCode(new IssueCodeImpl().setValue(issueCodeType))
                .setDiagnostics(new StringValueImpl().setValue(SdcctExceptionUtils.buildRootCauseStackTrace(exception)))
                .setSeverity(new IssueSeverityImpl().setValue(IssueSeverityType.ERROR));

        return Response.status(respStatus).entity(new OperationOutcomeImpl().setIssue(Collections.singletonList(opOutcomeIssue))).build();
    }
}
