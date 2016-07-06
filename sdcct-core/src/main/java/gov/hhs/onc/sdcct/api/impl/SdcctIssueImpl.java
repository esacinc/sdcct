package gov.hhs.onc.sdcct.api.impl;

import gov.hhs.onc.sdcct.api.IssueLevel;
import gov.hhs.onc.sdcct.api.SdcctIssue;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import javax.annotation.Nullable;

public class SdcctIssueImpl implements SdcctIssue {
    protected IssueLevel level;
    protected SdcctLocation loc;
    protected String msg;
    protected String src;

    public SdcctIssueImpl(IssueLevel level) {
        this.level = level;
    }

    @Override
    public IssueLevel getLevel() {
        return this.level;
    }

    @Override
    public boolean hasLocation() {
        return (this.loc != null);
    }

    @Nullable
    @Override
    public SdcctLocation getLocation() {
        return this.loc;
    }

    @Override
    public SdcctIssue setLocation(@Nullable SdcctLocation loc) {
        this.loc = loc;

        return this;
    }

    @Override
    public boolean hasMessage() {
        return (this.msg != null);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public SdcctIssue setMessage(@Nullable String msg) {
        this.msg = msg;

        return this;
    }

    @Override
    public boolean hasSource() {
        return (this.src != null);
    }

    @Nullable
    @Override
    public String getSource() {
        return this.src;
    }

    @Override
    public SdcctIssue setSource(@Nullable String src) {
        this.src = src;

        return this;
    }
}
