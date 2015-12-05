package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.context.SdcctProperties;
import gov.hhs.onc.sdcct.context.impl.AbstractApplicationInitializer;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.logging.LoggingInitializer;
import java.io.File;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.env.ConfigurableEnvironment;

public abstract class AbstractLoggingInitializer extends AbstractApplicationInitializer implements LoggingInitializer {
    protected final static String DEFAULT_LOG_FILE_DIR_RELATIVE_PATH = "logs";

    protected final static String DEFAULT_LOGSTASH_LOG_FILE_NAME_SUFFIX = "-logstash";

    protected AbstractLoggingInitializer(SdcctApplication app) {
        super(app);
    }

    @Override
    public void initialize(ConfigurableEnvironment env) {
        this.app.setLogConsoleTty(this.buildLogConsoleTty(env));
        this.app.setLogFileDirectory(this.buildLogFileDirectory(env));
        this.app.setLogFileName(this.buildLogFileName(env));
        this.app.setLogstashLogFileName(this.buildLogstashLogFileName(env));
    }

    protected String buildLogstashLogFileName(ConfigurableEnvironment env) {
        String logstashLogFileName =
            env.getProperty(SdcctProperties.LOGGING_LOGSTASH_FILE_NAME_NAME, (this.app.getName() + DEFAULT_LOGSTASH_LOG_FILE_NAME_SUFFIX));

        if (StringUtils.isBlank(logstashLogFileName)) {
            throw new ApplicationContextException("Unable to determine Logstash log file name.");
        }

        return logstashLogFileName;
    }

    protected boolean buildLogConsoleTty(ConfigurableEnvironment env) {
        String consoleTty = env.getProperty(SdcctProperties.LOGGING_CONSOLE_TTY_NAME);

        return ((consoleTty != null) ? BooleanUtils.toBoolean(consoleTty) : (System.console() != null));
    }

    protected String buildLogFileName(ConfigurableEnvironment env) {
        String logFileName = env.getProperty(SdcctProperties.LOGGING_FILE_NAME_NAME, this.app.getName());

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
        return env.getProperty(SdcctProperties.LOGGING_FILE_DIR_NAME, new File(this.app.getHomeDirectory(), DEFAULT_LOG_FILE_DIR_RELATIVE_PATH).getPath());
    }
}
