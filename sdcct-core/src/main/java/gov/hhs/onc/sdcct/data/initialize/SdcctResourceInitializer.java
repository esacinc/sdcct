package gov.hhs.onc.sdcct.data.initialize;

import gov.hhs.onc.sdcct.beans.LifecycleBean;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;

public interface SdcctResourceInitializer<T, U extends SdcctResource, V extends SdcctResourceRegistry<T, ?, U>> extends LifecycleBean {
    public void initialize();

    public boolean isEnabled();

    public void setEnabled(boolean enabled);

    public boolean isInitialized();
}
