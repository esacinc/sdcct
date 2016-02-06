package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.AppenderType;
import java.util.EnumMap;
import java.util.Map;
import javax.annotation.Nullable;

public class MessageMarker extends AbstractSdcctMarker {
    private final static long serialVersionUID = 0L;

    private Map<AppenderType, String> msgs = new EnumMap<>(AppenderType.class);

    public MessageMarker(String msg) {
        this(msg, null);
    }

    public MessageMarker(String msg, @Nullable String logstashFileMsg) {
        this(msg, msg, logstashFileMsg);
    }

    public MessageMarker(String consoleMsg, @Nullable String fileMsg, @Nullable String logstashFileMsg) {
        super("MSG");

        this.msgs.put(AppenderType.CONSOLE, consoleMsg);
        this.msgs.put(AppenderType.FILE, ((fileMsg != null) ? fileMsg : (fileMsg = consoleMsg)));
        this.msgs.put(AppenderType.LOGSTASH_FILE, ((logstashFileMsg != null) ? logstashFileMsg : fileMsg));
    }

    public Map<AppenderType, String> getMessages() {
        return this.msgs;
    }
}
