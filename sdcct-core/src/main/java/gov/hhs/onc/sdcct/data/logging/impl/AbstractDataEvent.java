package gov.hhs.onc.sdcct.data.logging.impl;

import gov.hhs.onc.sdcct.data.logging.DataEvent;
import gov.hhs.onc.sdcct.logging.impl.AbstractLoggingEvent;

public abstract class AbstractDataEvent extends AbstractLoggingEvent implements DataEvent {
    @Override
    protected String buildMarkerFieldName() {
        return "data";
    }
}
