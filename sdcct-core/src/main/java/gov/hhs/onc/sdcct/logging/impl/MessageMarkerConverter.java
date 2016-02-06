package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import gov.hhs.onc.sdcct.logging.AppenderType;
import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;

public class MessageMarkerConverter extends MessageConverter {
    public final static String WORD = "mMarker";

    private AppenderType appenderType;

    @Override
    public String convert(ILoggingEvent event) {
        MessageMarker msgMarker = SdcctStreamUtils.findInstance(SdcctMarkerUtils.stream(event.getMarker()), MessageMarker.class);

        return ((msgMarker != null) ? msgMarker.getMessages().get(this.appenderType) : super.convert(event));
    }

    @Override
    public void start() {
        this.appenderType = AppenderType.valueOf(this.getFirstOption().toUpperCase());

        super.start();
    }
}
