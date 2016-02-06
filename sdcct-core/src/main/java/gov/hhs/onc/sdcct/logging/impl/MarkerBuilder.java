package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.utils.SdcctMarkerUtils;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.builder.Builder;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MarkerBuilder implements Builder<Marker> {
    private Marker marker;

    public MarkerBuilder(String ... tags) {
        this.appendTags(tags);
    }

    public MarkerBuilder appendField(Object fieldValue) {
        return this.appendField(SdcctMarkerUtils.buildFieldName(fieldValue), fieldValue);
    }

    public MarkerBuilder appendField(String fieldName, Object fieldValue) {
        return this.appendMarker(new FieldMarker(fieldName, fieldValue));
    }

    public MarkerBuilder appendMessage(String msg) {
        return this.appendMessage(msg, null);
    }

    public MarkerBuilder appendMessage(String msg, @Nullable String logstashFileMsg) {
        return this.appendMessage(msg, null, logstashFileMsg);
    }

    public MarkerBuilder appendMessage(String consoleMsg, @Nullable String fileMsg, @Nullable String logstashFileMsg) {
        return this.appendMarker(new MessageMarker(consoleMsg, fileMsg, logstashFileMsg));
    }

    public MarkerBuilder appendTags(String ... tags) {
        Stream.of(tags).forEach(tag -> this.appendMarker(MarkerFactory.getDetachedMarker(tag)));

        return this;
    }

    public MarkerBuilder appendMarker(Marker markerAppend) {
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
