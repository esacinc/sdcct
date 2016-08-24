package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;

public class ValidationException extends SdcctException {
    private final static long serialVersionUID = 0L;

    private ValidationResult result;

    public ValidationException(@Nullable String msg, ValidationResult result) {
        this(msg, null, result);
    }

    public ValidationException(@Nullable Throwable cause, ValidationResult result) {
        this(null, cause, result);
    }

    public ValidationException(@Nullable String msg, @Nullable Throwable cause, ValidationResult result) {
        super(msg, cause);

        this.result = result;
    }

    public ValidationResult getResult() {
        return this.result;
    }
}
