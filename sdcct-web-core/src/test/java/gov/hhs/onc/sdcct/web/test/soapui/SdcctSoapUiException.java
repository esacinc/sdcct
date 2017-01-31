package gov.hhs.onc.sdcct.web.test.soapui;

import gov.hhs.onc.sdcct.SdcctException;
import javax.annotation.Nullable;

public class SdcctSoapUiException extends SdcctException {
    private final static long serialVersionUID = 0L;

    public SdcctSoapUiException(@Nullable String msg) {
        this(msg, null);
    }

    public SdcctSoapUiException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SdcctSoapUiException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
