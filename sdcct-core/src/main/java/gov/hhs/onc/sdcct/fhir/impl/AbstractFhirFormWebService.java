package gov.hhs.onc.sdcct.fhir.impl;

import gov.hhs.onc.sdcct.fhir.FhirFormWebService;
import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.fhir.OperationOutcomeIssue;
import gov.hhs.onc.sdcct.form.FormService;
import gov.hhs.onc.sdcct.form.impl.AbstractFormWebService;
import javax.annotation.Nullable;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.MessageContext;

public abstract class AbstractFhirFormWebService<T extends FormService> extends AbstractFormWebService<T> implements FhirFormWebService<T> {
    @Context
    protected MessageContext msgContext;
    
    protected static Response buildErrorResponse(IssueCodeType issueCodeType, String msg) {
        return buildErrorResponse(issueCodeType, msg, null);
    }
    
    protected static Response buildErrorResponse(IssueCodeType issueCodeType, String msg, @Nullable Exception cause) {
        OperationOutcomeIssue opOutcomeIssue = new OperationOutcomeIssueImpl();
        opOutcomeIssue.setCode(new IssueCodeImpl(null, null, issueCodeType));
        
        OperationOutcome opOutcome = new OperationOutcomeImpl();
        opOutcome.getIssue().add(opOutcomeIssue);
        
        return Response.serverError().entity(opOutcome).build();
    }
}
