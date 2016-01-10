package gov.hhs.onc.sdcct.beans.impl;

import gov.hhs.onc.sdcct.beans.NamedBean;
import javax.annotation.Nullable;

public abstract class AbstractNamedBean extends AbstractIdentifiedBean implements NamedBean {
    protected String name;

    @Nullable
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }
}
