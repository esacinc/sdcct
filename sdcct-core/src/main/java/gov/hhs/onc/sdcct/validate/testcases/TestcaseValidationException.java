package gov.hhs.onc.sdcct.validate.testcases;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;

public class TestcaseValidationException extends SdcctException {
    private final static long serialVersionUID = 0L;

    public TestcaseValidationException(@Nullable String msg) {
        this(msg, null);
    }

    public TestcaseValidationException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public TestcaseValidationException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
