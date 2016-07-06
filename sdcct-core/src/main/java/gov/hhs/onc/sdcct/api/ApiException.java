package gov.hhs.onc.sdcct.api;

import gov.hhs.onc.sdcct.SdcctException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public class ApiException extends SdcctException {
    protected StatusType respStatus = Status.INTERNAL_SERVER_ERROR;
    protected List<SdcctIssue> issues = new ArrayList<>();

    private final static long serialVersionUID = 0L;

    public ApiException(@Nullable String msg) {
        this(msg, null);
    }

    public ApiException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public ApiException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public ApiException addIssues(SdcctIssue ... issues) {
        Stream.of(issues).forEach(this.issues::add);

        return this;
    }

    public boolean hasIssues() {
        return !this.issues.isEmpty();
    }

    public List<SdcctIssue> getIssues() {
        return this.issues;
    }

    public ApiException setIssues(List<SdcctIssue> issues) {
        this.issues = issues;

        return this;
    }

    public StatusType getResponseStatus() {
        return this.respStatus;
    }

    public ApiException setResponseStatus(StatusType respStatus) {
        this.respStatus = respStatus;

        return this;
    }
}
