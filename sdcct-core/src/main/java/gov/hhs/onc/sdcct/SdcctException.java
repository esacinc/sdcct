package gov.hhs.onc.sdcct;

import javax.annotation.Nullable;

public class SdcctException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    public SdcctException(@Nullable String msg) {
        this(msg, null);
    }

    public SdcctException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SdcctException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
