package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.form.FormException;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;
import org.apache.commons.lang3.ArrayUtils;

public class FhirFormException extends FormException {
    private final static long serialVersionUID = 0L;

    private IssueType issueType = IssueType.EXCEPTION;
    private Object[] opOutcomeArgs;
    private OperationOutcomeType opOutcomeType;

    public FhirFormException(@Nullable String msg) {
        this(msg, null);
    }

    public FhirFormException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FhirFormException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public IssueType getIssueType() {
        return this.issueType;
    }

    public FhirFormException setIssueType(IssueType issueType) {
        this.issueType = issueType;

        return this;
    }

    public boolean hasOperationOutcomeArgs() {
        return !ArrayUtils.isEmpty(this.opOutcomeArgs);
    }

    @Nullable
    public Object[] getOperationOutcomeArgs() {
        return this.opOutcomeArgs;
    }

    public FhirFormException setOperationOutcomeArgs(@Nullable Object ... opOutcomeArgs) {
        this.opOutcomeArgs = opOutcomeArgs;

        return this;
    }

    public boolean hasOperationOutcomeType() {
        return (this.opOutcomeType != null);
    }

    @Nullable
    public OperationOutcomeType getOperationOutcomeType() {
        return this.opOutcomeType;
    }

    public FhirFormException setOperationOutcomeType(@Nullable OperationOutcomeType opOutcomeType) {
        this.opOutcomeType = opOutcomeType;

        return this;
    }

    @Override
    public FhirFormException setResponseStatus(StatusType respStatus) {
        return ((FhirFormException) super.setResponseStatus(respStatus));
    }
}
