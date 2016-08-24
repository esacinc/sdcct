package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.SdcctException;
import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import javax.annotation.Nullable;

public class WsException extends SdcctException {
    protected SdcctHttpStatus respStatus = SdcctHttpStatus.INTERNAL_SERVER_ERROR;

    private final static long serialVersionUID = 0L;

    public WsException() {
        this(((String) null));
    }

    public WsException(@Nullable String msg) {
        this(msg, null);
    }

    public WsException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public WsException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public SdcctHttpStatus getResponseStatus() {
        return this.respStatus;
    }

    public WsException setResponseStatus(SdcctHttpStatus respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
