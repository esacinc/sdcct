package gov.hhs.onc.sdcct.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import gov.hhs.onc.sdcct.logging.AppenderType;
import gov.hhs.onc.sdcct.logging.impl.MessageMarker;
import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.io.IOException;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;
import org.springframework.stereotype.Component;

@Component("jsonProvMarkerMsg")
public class MessageMarkerJsonProvider extends MessageJsonProvider {
    @Override
    public void writeTo(JsonGenerator jsonGen, ILoggingEvent event) throws IOException {
        MessageMarker msgMarker = SdcctStreamUtils.findInstance(SdcctMarkerUtils.stream(event.getMarker()), MessageMarker.class);

        if (msgMarker != null) {
            // noinspection ConstantConditions
            JsonWritingUtils.writeStringField(jsonGen, this.getFieldName(), msgMarker.getMessages().get(AppenderType.LOGSTASH_FILE));
        } else {
            super.writeTo(jsonGen, event);
        }
    }
}
