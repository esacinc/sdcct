package gov.hhs.onc.sdcct.validate.testcases.rfd;

import gov.hhs.onc.sdcct.validate.testcases.TestcaseValidationException;
import javax.annotation.Nullable;

public class IheTestcaseValidationException extends TestcaseValidationException {
    private final static long serialVersionUID = 0L;

    public IheTestcaseValidationException(@Nullable String msg) {
        this(msg, null);
    }

    public IheTestcaseValidationException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public IheTestcaseValidationException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
