package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum SearchParamType implements IdentifiedBean {
    COORD, DATE, NUMBER, QUANTITY, STRING, TOKEN, URI;

    private final String id;

    private SearchParamType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
