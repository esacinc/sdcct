package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum DbObjectType implements IdentifiedBean {
    SEQUENCE, TABLE;

    private final String id;

    private DbObjectType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
