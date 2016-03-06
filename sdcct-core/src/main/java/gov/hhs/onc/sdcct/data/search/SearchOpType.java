package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum SearchOpType implements IdentifiedBean {
    EQ, NE, GT, LT, GE, LE, SA, EB, AP;

    private final String id;

    private SearchOpType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
