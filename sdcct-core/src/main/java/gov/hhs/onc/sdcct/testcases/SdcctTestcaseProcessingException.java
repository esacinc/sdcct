package gov.hhs.onc.sdcct.testcases;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;
import org.springframework.validation.BindingResult;

public class SdcctTestcaseProcessingException extends SdcctException {
    private final static long serialVersionUID = 0L;

    private BindingResult bindingResult;

    public SdcctTestcaseProcessingException(@Nullable String msg) {
        this(msg, null);
    }

    public SdcctTestcaseProcessingException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SdcctTestcaseProcessingException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public boolean hasBindingResult() {
        return (this.bindingResult != null);
    }

    @Nullable
    public BindingResult getBindingResult() {
        return this.bindingResult;
    }

    public SdcctTestcaseProcessingException setBindingResult(@Nullable BindingResult bindingResult) {
        this.bindingResult = bindingResult;

        return this;
    }
}
