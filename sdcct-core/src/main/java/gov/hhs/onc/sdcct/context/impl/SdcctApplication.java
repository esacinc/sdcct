package gov.hhs.onc.sdcct.context.impl;

import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

public class SdcctApplication extends SpringApplication {
    public final static String BEAN_NAME = "app";

    private File dataDir;
    private File homeDir;
    private boolean logConsoleThreadName;
    private boolean logConsoleTty;
    private boolean logConsoleTx;
    private File logFileDir;
    private String logFileName;
    private String logstashLogFileName;
    private String name;
    private CompositePropertySource propSrc;

    public SdcctApplication(Object ... srcs) {
        super(srcs);
    }

    @Override
    protected void postProcessApplicationContext(ConfigurableApplicationContext appContext) {
        super.postProcessApplicationContext(appContext);

        ((GenericApplicationContext) appContext).getBeanFactory().registerSingleton(BEAN_NAME, this);
    }

    @Override
    protected void configurePropertySources(ConfigurableEnvironment env, String[] args) {
        super.configurePropertySources(env, args);

        env.getPropertySources().addLast(this.propSrc);
    }

    public File getDataDirectory() {
        return this.dataDir;
    }

    public void setDataDirectory(File dataDir) {
        this.dataDir = dataDir;
    }

    public File getHomeDirectory() {
        return this.homeDir;
    }

    public void setHomeDirectory(File homeDir) {
        this.homeDir = homeDir;
    }

    public boolean isLogConsoleThreadName() {
        return this.logConsoleThreadName;
    }

    public void setLogConsoleThreadName(boolean logConsoleThreadName) {
        this.logConsoleThreadName = logConsoleThreadName;
    }

    public boolean isLogConsoleTty() {
        return this.logConsoleTty;
    }

    public void setLogConsoleTty(boolean logConsoleTty) {
        this.logConsoleTty = logConsoleTty;
    }

    public boolean isLogConsoleTx() {
        return this.logConsoleTx;
    }

    public void setLogConsoleTx(boolean logConsoleTx) {
        this.logConsoleTx = logConsoleTx;
    }

    public File getLogFileDirectory() {
        return this.logFileDir;
    }

    public void setLogFileDirectory(File logFileDir) {
        this.logFileDir = logFileDir;
    }

    public String getLogFileName() {
        return this.logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getLogstashLogFileName() {
        return this.logstashLogFileName;
    }

    public void setLogstashLogFileName(String logstashLogFileName) {
        this.logstashLogFileName = logstashLogFileName;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompositePropertySource getPropertySource() {
        return this.propSrc;
    }

    public void setPropertySource(CompositePropertySource propSrc) {
        this.propSrc = propSrc;
    }
}
