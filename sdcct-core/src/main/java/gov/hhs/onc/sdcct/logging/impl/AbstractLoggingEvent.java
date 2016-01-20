package gov.hhs.onc.sdcct.logging.impl;

import gov.hhs.onc.sdcct.logging.LoggingEvent;
import org.slf4j.Marker;

public abstract class AbstractLoggingEvent implements LoggingEvent {
    @Override
    public Marker buildMarker() {
        return this.buildMarkerInternal().build();
    }

    protected SdcctMarkerBuilder buildMarkerInternal() {
        return new SdcctMarkerBuilder().appendField(this);
    }
}
