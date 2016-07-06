package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.api.ApiException;
import gov.hhs.onc.sdcct.api.SdcctIssue;
import java.util.List;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;

public class RfdException extends ApiException {
    private final static long serialVersionUID = 0L;

    public RfdException(@Nullable String msg) {
        this(msg, null);
    }

    public RfdException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public RfdException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    public RfdException addIssues(SdcctIssue ... issues) {
        return ((RfdException) super.addIssues(issues));
    }

    @Override
    public RfdException setIssues(List<SdcctIssue> issues) {
        return ((RfdException) super.setIssues(issues));
    }

    @Override
    public RfdException setResponseStatus(StatusType respStatus) {
        return ((RfdException) super.setResponseStatus(respStatus));
    }
}
