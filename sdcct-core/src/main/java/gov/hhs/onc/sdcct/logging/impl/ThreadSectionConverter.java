package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.ThreadConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import org.apache.commons.lang3.StringUtils;

public class ThreadSectionConverter extends ThreadConverter {
    public final static String WORD = "xT";

    private final static String SECTION_PREFIX = "[";
    private final static String SECTION_SUFFIX = "] ";

    private boolean enabled;

    @Override
    public String convert(ILoggingEvent event) {
        return (this.enabled ? (SECTION_PREFIX + super.convert(event) + SECTION_SUFFIX) : StringUtils.EMPTY);
    }

    @Override
    public void start() {
        this.enabled = ((SdcctApplication) this.getContext().getObject(SdcctApplication.BEAN_NAME)).isLogConsoleThreadName();

        super.start();
    }
}
