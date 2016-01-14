package gov.hhs.onc.sdcct.fhir;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.Status;

public class FhirException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    private IssueCodeType issueCodeType = IssueCodeType.EXCEPTION;
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

    public IssueCodeType getIssueCodeType() {
        return this.issueCodeType;
    }

    public FhirException setIssueCodeType(IssueCodeType issueCodeType) {
        this.issueCodeType = issueCodeType;

        return this;
    }

    public Status getResponseStatus() {
        return this.respStatus;
    }

    public FhirException setRespStatus(Status respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
