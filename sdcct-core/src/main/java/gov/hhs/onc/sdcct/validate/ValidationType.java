package gov.hhs.onc.sdcct.validate;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum ValidationType implements IdentifiedBean {
    SCHEMA, SCHEMATRON, TERMINOLOGY;

    private final String id;

    private ValidationType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
