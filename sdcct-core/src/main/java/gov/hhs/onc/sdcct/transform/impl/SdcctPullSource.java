package gov.hhs.onc.sdcct.transform.impl;

import gov.hhs.onc.sdcct.beans.LocationBean;
import gov.hhs.onc.sdcct.transform.utils.SdcctTransformUtils;
import javax.annotation.Nullable;
import net.sf.saxon.expr.parser.Location;
import net.sf.saxon.pull.PullProvider;
import net.sf.saxon.pull.PullSource;

public class SdcctPullSource extends PullSource implements LocationBean {
    public SdcctPullSource(@Nullable String publicId, PullProvider prov) {
        super(prov);

        this.setPublicId(publicId);
    }

    public boolean hasColumnNumber() {
        return (this.getColumnNumber() > 0);
    }

    @Override
    public int getColumnNumber() {
        // noinspection ConstantConditions
        return (this.hasSourceLocator() ? this.getSourceLocator().getColumnNumber() : -1);
    }

    public void setColumnNumber(int colNum) {
        if (this.hasSourceLocator()) {
            SdcctTransformUtils.setColumnNumber(this.getSourceLocator(), colNum);
        }
    }

    public boolean hasLineNumber() {
        return (this.getLineNumber() > 0);
    }

    @Override
    public int getLineNumber() {
        // noinspection ConstantConditions
        return (this.hasSourceLocator() ? this.getSourceLocator().getLineNumber() : -1);
    }

    public void setLineNumber(int lineNum) {
        if (this.hasSourceLocator()) {
            SdcctTransformUtils.setLineNumber(this.getSourceLocator(), lineNum);
        }
    }

    public boolean hasPublicId() {
        return (this.getPublicId() != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        // noinspection ConstantConditions
        return (this.hasSourceLocator() ? this.getSourceLocator().getPublicId() : null);
    }

    public void setPublicId(@Nullable String publicId) {
        if (this.hasSourceLocator()) {
            SdcctTransformUtils.setPublicId(this.getSourceLocator(), publicId);
        }
    }

    public boolean hasSourceLocator() {
        return (this.getSourceLocator() != null);
    }

    @Nullable
    public Location getSourceLocator() {
        return this.getPullProvider().getSourceLocator();
    }

    public boolean hasSystemId() {
        return (this.getSystemId() != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        // noinspection ConstantConditions
        return (this.hasSourceLocator() ? this.getSourceLocator().getSystemId() : null);
    }

    @Override
    public void setSystemId(@Nullable String sysId) {
        if (this.hasSourceLocator()) {
            SdcctTransformUtils.setSystemId(this.getSourceLocator(), sysId);
        }
    }
}
