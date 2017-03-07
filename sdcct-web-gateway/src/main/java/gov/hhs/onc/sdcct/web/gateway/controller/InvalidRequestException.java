package gov.hhs.onc.sdcct.web.gateway.controller;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;
import org.springframework.validation.Errors;

public class InvalidRequestException extends SdcctException {
    private Errors errors;

    private final static long serialVersionUID = 0L;

    public InvalidRequestException(@Nullable String msg) {
        this(msg, null);
    }

    public InvalidRequestException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public InvalidRequestException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public InvalidRequestException(@Nullable String msg, @Nullable Throwable cause, Errors errors) {
        super(msg, cause);

        this.errors = errors;
    }

    public Errors getErrors() {
        return this.errors;
    }
}
