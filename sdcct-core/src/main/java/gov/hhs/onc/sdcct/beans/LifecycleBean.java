package gov.hhs.onc.sdcct.beans;

import org.springframework.context.SmartLifecycle;

public interface LifecycleBean extends SmartLifecycle {
    public boolean canStop();
    
    public boolean canStart();
}
