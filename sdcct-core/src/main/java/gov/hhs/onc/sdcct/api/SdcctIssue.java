package gov.hhs.onc.sdcct.api;

import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;

public interface SdcctIssue {
    public IssueLevel getLevel();

    public boolean hasLocation();

    @Nullable
    public SdcctLocation getLocation();

    public SdcctIssue setLocation(@Nullable SdcctLocation loc);

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public SdcctIssue setMessage(@Nullable String msg);

    public boolean hasSource();

    @Nullable
    public String getSource();

    public SdcctIssue setSource(@Nullable String src);
}
