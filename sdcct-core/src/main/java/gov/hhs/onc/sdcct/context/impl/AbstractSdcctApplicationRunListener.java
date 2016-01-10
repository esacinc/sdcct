package gov.hhs.onc.sdcct.context.impl;

import javax.annotation.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class AbstractSdcctApplicationRunListener implements SpringApplicationRunListener {
    protected SdcctApplication app;
    protected String[] args;

    protected AbstractSdcctApplicationRunListener(SpringApplication app, String[] args) {
        this.app = ((SdcctApplication) app);
        this.args = args;
    }

    @Override
    public void finished(ConfigurableApplicationContext appContext, @Nullable Throwable exception) {
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext appContext) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment env) {
    }

    @Override
    public void started() {
    }
}
