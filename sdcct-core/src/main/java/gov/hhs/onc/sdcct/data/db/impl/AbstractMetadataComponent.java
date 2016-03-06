package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.beans.NamedBean;

public abstract class AbstractMetadataComponent implements NamedBean {
    protected String name;
    protected boolean indexed;

    protected AbstractMetadataComponent(String name, boolean indexed) {
        this.name = name;
        this.indexed = indexed;
    }

    public boolean isIndexed() {
        return this.indexed;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
