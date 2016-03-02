package gov.hhs.onc.sdcct.fhir.ws;

import gov.hhs.onc.sdcct.fhir.IssueCodeType;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class FhirWebServiceException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    private IssueCodeType issueCodeType = IssueCodeType.EXCEPTION;
    private Pair<String, Object[]> issueDetailConceptParts;
    private Status respStatus = Status.INTERNAL_SERVER_ERROR;

    public FhirWebServiceException(@Nullable String msg) {
        this(msg, null);
    }

    public FhirWebServiceException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FhirWebServiceException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public IssueCodeType getIssueCodeType() {
        return this.issueCodeType;
    }

    public FhirWebServiceException setIssueCodeType(IssueCodeType issueCodeType) {
        this.issueCodeType = issueCodeType;

        return this;
    }

    public boolean hasIssueDetailConceptParts() {
        return (this.issueDetailConceptParts != null);
    }

    public Pair<String, Object[]> getIssueDetailConceptParts() {
        return this.issueDetailConceptParts;
    }

    public FhirWebServiceException setIssueDetailConceptParts(String issueDetailConceptCode, Object ... issueDetailConceptDisplayArgs) {
        this.issueDetailConceptParts = new ImmutablePair<>(issueDetailConceptCode, issueDetailConceptDisplayArgs);

        return this;
    }

    public Status getResponseStatus() {
        return this.respStatus;
    }

    public FhirWebServiceException setResponseStatus(Status respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
