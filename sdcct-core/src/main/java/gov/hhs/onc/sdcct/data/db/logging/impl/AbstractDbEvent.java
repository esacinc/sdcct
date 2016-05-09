package gov.hhs.onc.sdcct.data.db.logging.impl;

import gov.hhs.onc.sdcct.data.db.logging.DbEvent;
import gov.hhs.onc.sdcct.data.logging.impl.AbstractDataEvent;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public abstract class AbstractDbEvent extends AbstractDataEvent implements DbEvent {
    @Override
    protected String buildMarkerFieldName() {
        return (super.buildMarkerFieldName() + SdcctStringUtils.PERIOD + "db");
    }
}
