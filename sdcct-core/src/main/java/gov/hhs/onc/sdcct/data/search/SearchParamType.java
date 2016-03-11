package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.db.DbTableNames;

public enum SearchParamType implements IdentifiedBean {
    COORD(DbTableNames.SEARCH_PARAM_COORD), DATE(DbTableNames.SEARCH_PARAM_DATE), NUM(DbTableNames.SEARCH_PARAM_NUM), QUANTITY(
        DbTableNames.SEARCH_PARAM_QUANTITY), REF(DbTableNames.SEARCH_PARAM_REF), STR(DbTableNames.SEARCH_PARAM_STR), TOKEN(DbTableNames.SEARCH_PARAM_TOKEN),
    URI(DbTableNames.SEARCH_PARAM_URI);

    private final String id;

    private SearchParamType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
