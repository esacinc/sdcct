package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.api.ApiException;
import gov.hhs.onc.sdcct.api.SdcctIssue;
import java.util.List;
import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;

public class FhirException extends ApiException {
    private final static long serialVersionUID = 0L;

    public FhirException(@Nullable String msg) {
        this(msg, null);
    }

    public FhirException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FhirException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override
    public FhirException addIssues(SdcctIssue ... issues) {
        return ((FhirException) super.addIssues(issues));
    }

    @Override
    public FhirException setIssues(List<SdcctIssue> issues) {
        return ((FhirException) super.setIssues(issues));
    }

    @Override
    public FhirException setResponseStatus(StatusType respStatus) {
        return ((FhirException) super.setResponseStatus(respStatus));
    }
}
