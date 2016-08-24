package gov.hhs.onc.sdcct.transform;

import gov.hhs.onc.sdcct.SdcctException;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;

public class SdcctTransformException extends SdcctException {
    protected SdcctLocation loc;

    private final static long serialVersionUID = 0L;

    public SdcctTransformException(@Nullable String msg) {
        this(msg, null);
    }

    public SdcctTransformException(@Nullable Throwable cause) {
        this(null, cause);
    }

    public SdcctTransformException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    public boolean hasLocation() {
        return (this.loc != null);
    }

    @Nullable
    public SdcctLocation getLocation() {
        return this.loc;
    }

    public SdcctTransformException setLocation(@Nullable SdcctLocation loc) {
        this.loc = loc;

        return this;
    }
}
