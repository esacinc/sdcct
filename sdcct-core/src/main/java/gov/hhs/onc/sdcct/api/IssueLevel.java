package gov.hhs.onc.sdcct.api;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum IssueLevel implements IdentifiedBean {
    INFORMATION, WARNING, ERROR, FATAL;

    private final String id;

    private IssueLevel() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
