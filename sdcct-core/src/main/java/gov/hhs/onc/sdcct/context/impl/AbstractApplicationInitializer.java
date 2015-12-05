package gov.hhs.onc.sdcct.context.impl;

import gov.hhs.onc.sdcct.context.ApplicationInitializer;

public abstract class AbstractApplicationInitializer implements ApplicationInitializer {
    protected SdcctApplication app;

    protected AbstractApplicationInitializer(SdcctApplication app) {
        this.app = app;
    }

    @Override
    public SdcctApplication getApplication() {
        return this.app;
    }
}
