package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import javax.annotation.Nullable;

public abstract class AbstractIdentifiedBean implements IdentifiedBean {
    protected String id;

    @Nullable
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(@Nullable String id) {
        this.id = id;
    }
}
