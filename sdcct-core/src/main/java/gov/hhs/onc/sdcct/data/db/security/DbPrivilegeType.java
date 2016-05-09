package gov.hhs.onc.sdcct.data.db.security;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum DbPrivilegeType implements IdentifiedBean {
    ALL, USAGE, SELECT, INSERT, UPDATE, DELETE;

    private final String id;

    private DbPrivilegeType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
