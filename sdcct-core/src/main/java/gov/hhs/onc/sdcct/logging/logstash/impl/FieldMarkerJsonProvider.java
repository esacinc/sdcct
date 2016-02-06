package gov.hhs.onc.sdcct.logging.logstash.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.core.JsonGenerator;
import gov.hhs.onc.sdcct.logging.impl.FieldMarker;
import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.io.IOException;
import java.util.stream.Collectors;
import net.logstash.logback.composite.AbstractJsonProvider;
import org.springframework.stereotype.Component;

@Component("jsonProvMarkerField")
public class FieldMarkerJsonProvider extends AbstractJsonProvider<ILoggingEvent> {
    @Override
    public void writeTo(JsonGenerator jsonGen, ILoggingEvent event) throws IOException {
        SdcctMarkerUtils.serializeFields(jsonGen, SdcctStreamUtils.asInstances(SdcctMarkerUtils.stream(event.getMarker()), FieldMarker.class)
            .collect(Collectors.toMap(FieldMarker::getFieldName, FieldMarker::getFieldValue)));
    }
}
