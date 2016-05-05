package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.beans.LifecycleBean;

public abstract class AbstractLifecycleBean implements LifecycleBean {
    protected boolean autoStartup = true;
    protected int phase;

    @Override
    public void stop(Runnable stopCallback) {
        this.stop();

        stopCallback.run();
    }

    @Override
    public boolean isAutoStartup() {
        return this.autoStartup;
    }

    public void setAutoStartup(boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    @Override
    public int getPhase() {
        return this.phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
