package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.beans.LifecycleBean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractLifecycleBean implements LifecycleBean {
    protected final Lock lock;
    protected boolean autoStartup = true;
    protected int phase;

    protected AbstractLifecycleBean() {
        this.lock = new ReentrantLock();
    }

    @Override
    public void stop(Runnable stopCallback) {
        this.stop();

        stopCallback.run();
    }

    @Override
    public void stop() {
        this.lock.lock();

        try {
            if (this.canStop()) {
                this.stopInternal();
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void start() {
        this.lock.lock();

        try {
            if (this.canStart()) {
                this.startInternal();
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean canStop() {
        return this.isRunning();
    }

    @Override
    public boolean canStart() {
        return !this.isRunning();
    }

    protected abstract void stopInternal();

    protected abstract void startInternal();

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
