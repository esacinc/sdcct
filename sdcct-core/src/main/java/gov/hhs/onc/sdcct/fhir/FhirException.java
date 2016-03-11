package gov.hhs.onc.sdcct.fhir;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class FhirException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    private IssueTypeList issueType = IssueTypeList.EXCEPTION;
    private Pair<String, Object[]> issueDetailConceptParts;
    private Status respStatus = Status.INTERNAL_SERVER_ERROR;

    public FhirException(@Nullable String msg) {
        this(msg, null);
    }

    public FhirException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FhirException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public boolean hasIssueDetailConceptParts() {
        return (this.issueDetailConceptParts != null);
    }

    public Pair<String, Object[]> getIssueDetailConceptParts() {
        return this.issueDetailConceptParts;
    }

    public FhirException setIssueDetailConceptParts(String issueDetailConceptCode, Object ... issueDetailConceptDisplayArgs) {
        this.issueDetailConceptParts = new ImmutablePair<>(issueDetailConceptCode, issueDetailConceptDisplayArgs);

        return this;
    }

    public IssueTypeList getIssueType() {
        return this.issueType;
    }

    public FhirException setIssueType(IssueTypeList issueType) {
        this.issueType = issueType;

        return this;
    }

    public Status getResponseStatus() {
        return this.respStatus;
    }

    public FhirException setResponseStatus(Status respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
