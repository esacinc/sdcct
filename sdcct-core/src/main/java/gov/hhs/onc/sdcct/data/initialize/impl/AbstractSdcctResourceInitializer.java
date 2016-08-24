package gov.hhs.onc.sdcct.data.initialize.impl;

import gov.hhs.onc.sdcct.beans.impl.AbstractLifecycleBean;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.initialize.SdcctResourceInitializer;

public abstract class AbstractSdcctResourceInitializer<T, U extends SdcctResource, V extends SdcctResourceRegistry<T, ?, U>> extends AbstractLifecycleBean
    implements SdcctResourceInitializer<T, U, V> {
    protected V registry;
    protected boolean enabled = true;
    protected boolean initialized;

    protected AbstractSdcctResourceInitializer(V registry) {
        this.registry = registry;
    }

    @Override
    public void initialize() {
        if (!this.enabled || this.initialized) {
            return;
        }

        this.initializeInternal();

        this.initialized = true;
    }

    @Override
    protected void stopInternal() {
    }

    @Override
    protected void startInternal() {
        this.initialize();
    }

    protected abstract void initializeInternal();

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }
}
