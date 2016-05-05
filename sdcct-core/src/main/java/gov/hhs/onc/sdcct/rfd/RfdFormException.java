package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.form.FormException;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;

public class RfdFormException extends FormException {
    private final static long serialVersionUID = 0L;

    public RfdFormException(@Nullable String msg) {
        this(msg, null);
    }

    public RfdFormException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public RfdFormException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    public RfdFormException setResponseStatus(StatusType respStatus) {
        return ((RfdFormException) super.setResponseStatus(respStatus));
    }
}
