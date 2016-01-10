package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.logstash.logback.marker.ObjectAppendingMarker;
import org.apache.commons.lang3.builder.Builder;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class SdcctMarkerBuilder implements Builder<Marker> {
    private Marker marker;

    public SdcctMarkerBuilder(String ... tags) {
        this.appendTags(tags);
    }

    public SdcctMarkerBuilder appendField(Object fieldValue) {
        return this.appendField(SdcctMarkerUtils.buildFieldName(fieldValue), fieldValue);
    }

    public SdcctMarkerBuilder appendField(String fieldName, Object fieldValue) {
        return this.appendMarker(new ObjectAppendingMarker(fieldName, fieldValue));
    }

    public SdcctMarkerBuilder appendMessage(String msg) {
        return this.appendMessage(msg, null);
    }

    public SdcctMarkerBuilder appendMessage(String msg, @Nullable String logstashFileMsg) {
        return this.appendMessage(msg, null, logstashFileMsg);
    }

    public SdcctMarkerBuilder appendMessage(String consoleMsg, @Nullable String fileMsg, @Nullable String logstashFileMsg) {
        return this.appendMarker(new MessageMarker(consoleMsg, fileMsg, logstashFileMsg));
    }

    public SdcctMarkerBuilder appendTags(String ... tags) {
        Stream.of(tags).forEach(tag -> this.appendMarker(MarkerFactory.getDetachedMarker(tag)));

        return this;
    }

    public SdcctMarkerBuilder appendMarker(Marker markerAppend) {
        if (this.marker == null) {
            this.marker = markerAppend;
        } else {
            this.marker.add(markerAppend);
        }

        return this;
    }

    @Override
    public Marker build() {
        return this.marker;
    }
}
