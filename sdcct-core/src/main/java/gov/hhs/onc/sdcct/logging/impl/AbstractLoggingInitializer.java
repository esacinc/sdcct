package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.context.impl.AbstractApplicationInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.logging.LoggingInitializer;
import gov.hhs.onc.sdcct.config.utils.SdcctOptionUtils;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class AbstractLoggingInitializer extends AbstractApplicationInitializer implements LoggingInitializer {
    protected final static String DEFAULT_LOG_FILE_DIR_RELATIVE_PATH = "logs";
    protected final static String DEFAULT_DATA_DIR_RELATIVE_PATH = "var";

    protected final static String DEFAULT_LOGSTASH_LOG_FILE_NAME_SUFFIX = "-logstash";

    protected AbstractLoggingInitializer(SdcctApplication app) {
        super(app);
    }

    @Override
    public void initialize(ConfigurableEnvironment env) {
        this.app.setLogConsoleThreadName(this.buildLogConsoleThreadName(env));
        this.app.setLogConsoleTty(this.buildLogConsoleTty(env));
        this.app.setLogConsoleTx(this.buildLogConsoleTx(env));
        this.app.setLogFileDirectory(this.buildLogFileDirectory(env));
        this.app.setLogFileName(this.buildLogFileName(env));
        this.app.setLogstashLogFileName(this.buildLogstashLogFileName(env));
        this.app.setDataDirectory(this.buildDataDirectory(env));
    }

    protected File buildDataDirectory(ConfigurableEnvironment env) {
        String dataDirPath = this.buildDataDirectoryPath(env);

        if (StringUtils.isBlank(dataDirPath)) {
            throw new ApplicationContextException("Unable to determine data directory path.");
        }

        File dataDir = new File(dataDirPath);

        dataDirPath = dataDir.getPath();

        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                throw new ApplicationContextException(String.format("Unable to create data directory (path=%s).", dataDirPath));
            }
        } else if (!dataDir.isDirectory()) {
            throw new ApplicationContextException(String.format("Data directory path (%s) is not a directory.", dataDirPath));
        }

        return dataDir;
    }

    protected String buildDataDirectoryPath(ConfigurableEnvironment env) {
        return env.getProperty(SdcctPropertyNames.DATA_DIR, new File(this.app.getHomeDirectory(), DEFAULT_DATA_DIR_RELATIVE_PATH).getPath());
    }

    protected String buildLogstashLogFileName(ConfigurableEnvironment env) {
        String logstashLogFileName =
            env.getProperty(SdcctPropertyNames.LOGGING_LOGSTASH_FILE_NAME, (this.app.getName() + DEFAULT_LOGSTASH_LOG_FILE_NAME_SUFFIX));

        if (StringUtils.isBlank(logstashLogFileName)) {
            throw new ApplicationContextException("Unable to determine Logstash log file name.");
        }

        return logstashLogFileName;
    }

    protected boolean buildLogConsoleThreadName(ConfigurableEnvironment env) {
        return SdcctOptionUtils
            .getBooleanValue(SdcctPropertyNames.LOGGING_CONSOLE_THREAD_NAME, env.getProperty(SdcctPropertyNames.LOGGING_CONSOLE_THREAD_NAME));
    }

    protected boolean buildLogConsoleTty(ConfigurableEnvironment env) {
        return SdcctOptionUtils.getBooleanValue(SdcctPropertyNames.LOGGING_CONSOLE_TTY, env.getProperty(SdcctPropertyNames.LOGGING_CONSOLE_TTY),
            (System.console() != null));
    }

    protected boolean buildLogConsoleTx(ConfigurableEnvironment env) {
        return SdcctOptionUtils.getBooleanValue(SdcctPropertyNames.LOGGING_CONSOLE_TX, env.getProperty(SdcctPropertyNames.LOGGING_CONSOLE_TX));
    }

    protected String buildLogFileName(ConfigurableEnvironment env) {
        String logFileName = env.getProperty(SdcctPropertyNames.LOGGING_FILE_NAME, this.app.getName());

        if (StringUtils.isBlank(logFileName)) {
            throw new ApplicationContextException("Unable to determine log file name.");
        }

        return logFileName;
    }

    protected File buildLogFileDirectory(ConfigurableEnvironment env) {
        String logFileDirPath = this.buildLogFileDirectoryPath(env);

        if (StringUtils.isBlank(logFileDirPath)) {
            throw new ApplicationContextException("Unable to determine log file directory path.");
        }

        File logFileDir = new File(logFileDirPath);

        logFileDirPath = logFileDir.getPath();

        if (!logFileDir.exists()) {
            if (!logFileDir.mkdirs()) {
                throw new ApplicationContextException(String.format("Unable to create log file directory (path=%s).", logFileDirPath));
            }
        } else if (!logFileDir.isDirectory()) {
            throw new ApplicationContextException(String.format("Log file directory path (%s) is not a directory.", logFileDirPath));
        }

        return logFileDir;
    }

    protected String buildLogFileDirectoryPath(ConfigurableEnvironment env) {
        return env.getProperty(SdcctPropertyNames.LOGGING_FILE_DIR, new File(this.app.getHomeDirectory(), DEFAULT_LOG_FILE_DIR_RELATIVE_PATH).getPath());
    }
}
