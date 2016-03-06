package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "searchParamStr")
@Table(name = DbTableNames.SEARCH_PARAM_STR)
public class StringSearchParamImpl extends AbstractSearchParam implements StringSearchParam {
    private String value;

    public StringSearchParamImpl(@Nullable Long resourceEntityId, String name, String value) {
        super(SearchParamType.STRING, resourceEntityId, name);

        this.value = value;
    }

    public StringSearchParamImpl() {
        super(SearchParamType.STRING);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
