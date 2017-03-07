package gov.hhs.onc.sdcct.validate.testcases;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;

public class SdcctTestcaseValidationException extends SdcctException {
    private final static long serialVersionUID = 0L;

    public SdcctTestcaseValidationException(@Nullable String msg) {
        this(msg, null);
    }

    public SdcctTestcaseValidationException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SdcctTestcaseValidationException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
