package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class TxMdcConverter extends ClassicConverter {
    public final static String WORD = "xTx";

    private final static String SECTION_PREFIX = " {";
    private final static String SECTION_SUFFIX = "}";

    private final static String MDC_PROP_DELIM = ", ";

    private final static Map<String, String> MDC_PROP_DISPLAY_NAMES = new LinkedHashMap<String, String>(4) {
        private final static long serialVersionUID = 0L;

        {
            this.put(SdcctPropertyNames.HTTP_SERVER_TX_ID, "httpServer");
            this.put(SdcctPropertyNames.HTTP_CLIENT_TX_ID, "httpClient");
            this.put(SdcctPropertyNames.WS_SERVER_TX_ID, "wsServer");
            this.put(SdcctPropertyNames.WS_CLIENT_TX_ID, "wsClient");
        }
    };

    private boolean enabled;

    @Override
    public String convert(ILoggingEvent event) {
        if (!this.enabled) {
            return StringUtils.EMPTY;
        }

        final Map<String, String> mdcProps = event.getMDCPropertyMap();

        return (!mdcProps.isEmpty()
            ? MDC_PROP_DISPLAY_NAMES.entrySet().stream().filter(mdcPropDisplayNameEntry -> mdcProps.containsKey(mdcPropDisplayNameEntry.getKey()))
                .map(mdcPropDisplayNameEntry -> (mdcPropDisplayNameEntry.getValue() + SdcctStringUtils.EQUALS_CHAR
                    + mdcProps.get(mdcPropDisplayNameEntry.getKey())))
                .collect(Collectors.joining(MDC_PROP_DELIM, SECTION_PREFIX, SECTION_SUFFIX))
            : StringUtils.EMPTY);
    }

    @Override
    public void start() {
        this.enabled = ((SdcctApplication) this.getContext().getObject(SdcctApplication.BEAN_NAME)).isLogConsoleTty();

        super.start();
    }
}
