package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class TxMdcConverter extends ClassicConverter {
    public final static String WORD = "xTx";

    private final static String SECTION_PREFIX = " {";
    private final static String SECTION_SUFFIX = "}";

    @Override
    public String convert(ILoggingEvent event) {
        Map<String, String> mdcProps = event.getMDCPropertyMap();

        // TODO: implement

        return StringUtils.EMPTY;
    }
}
