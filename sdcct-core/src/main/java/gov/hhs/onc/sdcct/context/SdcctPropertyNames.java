package gov.hhs.onc.sdcct.context;

public final class SdcctPropertyNames {
    public final static String CLIENT_PREFIX = "client.";
    public final static String FILE_PREFIX = "file.";
    public final static String SERVER_PREFIX = "server.";
    public final static String PREFIX = "sdcct.";
    public final static String APP_PREFIX = PREFIX + "app.";
    public final static String HTTP_PREFIX = PREFIX + "http.";
    public final static String HTTP_CLIENT_PREFIX = HTTP_PREFIX + CLIENT_PREFIX;
    public final static String HTTP_SERVER_PREFIX = HTTP_PREFIX + SERVER_PREFIX;
    public final static String LOGGING_PREFIX = PREFIX + "logging.";
    public final static String LOGGING_CONSOLE_PREFIX = LOGGING_PREFIX + "console.";
    public final static String LOGGING_FILE_PREFIX = LOGGING_PREFIX + FILE_PREFIX;
    public final static String LOGGING_LOGGER_PREFIX = LOGGING_PREFIX + "logger.";
    public final static String LOGGING_LOGSTASH_PREFIX = LOGGING_PREFIX + "logstash.";
    public final static String LOGGING_LOGSTASH_FILE_PREFIX = LOGGING_LOGSTASH_PREFIX + FILE_PREFIX;
    public final static String WS_PREFIX = PREFIX + "ws.";
    public final static String WS_CLIENT_PREFIX = WS_PREFIX + CLIENT_PREFIX;
    public final static String WS_SERVER_PREFIX = WS_PREFIX + SERVER_PREFIX;

    public final static String DIR_SUFFIX = "dir";
    public final static String ID_SUFFIX = "id";
    public final static String NAME_SUFFIX = "name";
    public final static String TX_SUFFIX = "tx";
    public final static String TX_ID_SUFFIX = TX_SUFFIX + "." + ID_SUFFIX;

    public final static String USER_DIR = "user." + DIR_SUFFIX;

    public final static String APP_HOME_DIR = APP_PREFIX + "home." + DIR_SUFFIX;

    public final static String HTTP_CLIENT_TX_ID = HTTP_CLIENT_PREFIX + TX_ID_SUFFIX;

    public final static String HTTP_SERVER_TX_ID = HTTP_SERVER_PREFIX + TX_ID_SUFFIX;

    public final static String LOGGING_CONSOLE_THREAD_NAME = LOGGING_CONSOLE_PREFIX + "thread." + NAME_SUFFIX;
    public final static String LOGGING_CONSOLE_TTY = LOGGING_CONSOLE_PREFIX + "tty";
    public final static String LOGGING_CONSOLE_TX = LOGGING_CONSOLE_PREFIX + TX_SUFFIX;

    public final static String LOGGING_FILE_DIR = LOGGING_FILE_PREFIX + DIR_SUFFIX;
    public final static String LOGGING_FILE_NAME = LOGGING_FILE_PREFIX + NAME_SUFFIX;

    public final static String LOGGING_LOGSTASH_FILE_NAME = LOGGING_LOGSTASH_FILE_PREFIX + NAME_SUFFIX;

    public final static String WS_CLIENT_TX_ID = WS_CLIENT_PREFIX + TX_ID_SUFFIX;

    public final static String WS_SERVER_TX_ID = WS_SERVER_PREFIX + TX_ID_SUFFIX;

    private SdcctPropertyNames() {
    }
}
