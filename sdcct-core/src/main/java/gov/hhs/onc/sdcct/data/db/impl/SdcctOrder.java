package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import javax.annotation.Nullable;

public class SdcctOrder extends AbstractSdcctCriteriaComponent<SdcctOrder> {
    private boolean ascending;

    public SdcctOrder(String propName, boolean ascending) {
        this(null, propName, ascending);
    }

    public SdcctOrder(@Nullable Class<? extends SdcctEntity> entityClass, String propName, boolean ascending) {
        super(entityClass, propName);

        this.ascending = ascending;
    }

    public boolean isAscending() {
        return this.ascending;
    }

    public SdcctOrder setAscending(boolean ascending) {
        this.ascending = ascending;

        return this;
    }
}
