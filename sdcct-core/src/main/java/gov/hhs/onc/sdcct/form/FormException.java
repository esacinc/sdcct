package gov.hhs.onc.sdcct.form;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public class FormException extends RuntimeException {
    private final static long serialVersionUID = 0L;

    protected StatusType respStatus = Status.INTERNAL_SERVER_ERROR;

    public FormException(@Nullable String msg) {
        this(msg, null);
    }

    public FormException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FormException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public StatusType getResponseStatus() {
        return this.respStatus;
    }

    public FormException setResponseStatus(StatusType respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
