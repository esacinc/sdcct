package gov.hhs.onc.sdcct.rfd.ws;

import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import gov.hhs.onc.sdcct.ws.WsException;
import javax.annotation.Nullable;

public class RfdWsException extends WsException {
    private final static long serialVersionUID = 0L;

    public RfdWsException() {
        this(((String) null));
    }

    public RfdWsException(@Nullable String msg) {
        this(msg, null);
    }

    public RfdWsException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public RfdWsException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    public RfdWsException setResponseStatus(SdcctHttpStatus respStatus) {
        return ((RfdWsException) super.setResponseStatus(respStatus));
    }
}
