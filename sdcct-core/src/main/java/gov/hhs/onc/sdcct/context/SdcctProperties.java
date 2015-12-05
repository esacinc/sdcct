package gov.hhs.onc.sdcct.context;

public final class SdcctProperties {
    public final static String PREFIX = "sdcct.";
    public final static String APP_PREFIX = PREFIX + "app.";
    public final static String FILE_PREFIX = "file.";
    public final static String LOGGING_PREFIX = PREFIX + "logging.";
    public final static String LOGGING_CONSOLE_PREFIX = LOGGING_PREFIX + "console.";
    public final static String LOGGING_FILE_PREFIX = LOGGING_PREFIX + FILE_PREFIX;
    public final static String LOGGING_LOGGER_PREFIX = LOGGING_PREFIX + "logger.";
    public final static String LOGGING_LOGSTASH_PREFIX = LOGGING_PREFIX + "logstash.";
    public final static String LOGGING_LOGSTASH_FILE_PREFIX = LOGGING_LOGSTASH_PREFIX + FILE_PREFIX;

    public final static String DIR_SUFFIX = "dir";
    public final static String NAME_SUFFIX = "name";

    public final static String USER_DIR_NAME = "user." + DIR_SUFFIX;

    public final static String APP_HOME_DIR_NAME = APP_PREFIX + "home." + DIR_SUFFIX;

    public final static String LOGGING_CONSOLE_TTY_NAME = LOGGING_CONSOLE_PREFIX + "tty";

    public final static String LOGGING_FILE_DIR_NAME = LOGGING_FILE_PREFIX + DIR_SUFFIX;
    public final static String LOGGING_FILE_NAME_NAME = LOGGING_FILE_PREFIX + NAME_SUFFIX;

    public final static String LOGGING_LOGSTASH_FILE_NAME_NAME = LOGGING_LOGSTASH_FILE_PREFIX + NAME_SUFFIX;

    private SdcctProperties() {
    }
}
