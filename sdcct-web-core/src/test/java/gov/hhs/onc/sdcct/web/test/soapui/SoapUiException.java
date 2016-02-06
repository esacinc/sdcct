package gov.hhs.onc.sdcct.web.test.soapui;

import javax.annotation.Nullable;

public class SoapUiException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    public SoapUiException(@Nullable String msg) {
        this(msg, null);
    }

    public SoapUiException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SoapUiException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
