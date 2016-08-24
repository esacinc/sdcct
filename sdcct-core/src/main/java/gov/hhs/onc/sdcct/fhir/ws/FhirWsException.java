package gov.hhs.onc.sdcct.fhir.ws;

import gov.hhs.onc.sdcct.fhir.OperationOutcome;
import gov.hhs.onc.sdcct.net.http.SdcctHttpStatus;
import gov.hhs.onc.sdcct.ws.WsException;
import javax.annotation.Nullable;

public class FhirWsException extends WsException {
    private final static long serialVersionUID = 0L;

    private OperationOutcome opOutcome;

    public FhirWsException() {
        this(((String) null));
    }

    public FhirWsException(@Nullable String msg) {
        this(msg, null);
    }

    public FhirWsException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public FhirWsException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public boolean hasOperationOutcome() {
        return (this.opOutcome != null);
    }

    @Nullable
    public OperationOutcome getOperationOutcome() {
        return this.opOutcome;
    }

    public FhirWsException setOperationOutcome(@Nullable OperationOutcome opOutcome) {
        this.opOutcome = opOutcome;

        return this;
    }

    @Override
    public FhirWsException setResponseStatus(SdcctHttpStatus respStatus) {
        return ((FhirWsException) super.setResponseStatus(respStatus));
    }
}
