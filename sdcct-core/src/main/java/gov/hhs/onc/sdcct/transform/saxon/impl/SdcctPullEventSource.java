package gov.hhs.onc.sdcct.transform.saxon.impl;

import gov.hhs.onc.sdcct.beans.PublicIdentifiedBean;
import gov.hhs.onc.sdcct.beans.SystemIdentifiedBean;
import javax.annotation.Nullable;
import net.sf.saxon.evpull.EventIterator;
import net.sf.saxon.evpull.PullEventSource;

public class SdcctPullEventSource extends PullEventSource implements PublicIdentifiedBean, SystemIdentifiedBean {
    private String publicId;

    public SdcctPullEventSource(@Nullable String publicId, @Nullable String sysId, EventIterator prov) {
        super(prov);

        this.publicId = publicId;

        this.setSystemId(sysId);
    }

    public boolean hasPublicId() {
        return (this.publicId != null);
    }

    @Nullable
    @Override
    public String getPublicId() {
        return this.publicId;
    }

    public void setPublicId(@Nullable String publicId) {
        this.publicId = publicId;
    }

    public boolean hasSystemId() {
        return (this.getSystemId() != null);
    }

    @Nullable
    @Override
    public String getSystemId() {
        return super.getSystemId();
    }

    @Override
    public void setSystemId(@Nullable String sysId) {
        super.setSystemId(sysId);
    }
}
