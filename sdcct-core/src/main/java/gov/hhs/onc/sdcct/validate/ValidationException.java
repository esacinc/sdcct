package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.SdcctException;
import java.util.List;
import javax.annotation.Nullable;

public class ValidationException extends SdcctException {
    private final static long serialVersionUID = 0L;

    private List<ValidationIssue> issues;

    public ValidationException(@Nullable String msg, List<ValidationIssue> issues) {
        this(msg, null, issues);
    }

    public ValidationException(@Nullable Throwable cause, List<ValidationIssue> issues) {
        this(null, cause, issues);
    }

    public ValidationException(@Nullable String msg, @Nullable Throwable cause, List<ValidationIssue> issues) {
        super(msg, cause);

        this.issues = issues;
    }

    public List<ValidationIssue> getIssues() {
        return this.issues;
    }
}
