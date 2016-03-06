package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "searchParamDate")
@Table(name = DbTableNames.SEARCH_PARAM_DATE)
public class DateSearchParamImpl extends AbstractSearchParam implements DateSearchParam {
    private Date value;

    public DateSearchParamImpl(@Nullable Long resourceEntityId, String name, Date value) {
        super(SearchParamType.DATE, resourceEntityId, name);

        this.value = value;
    }

    public DateSearchParamImpl() {
        super(SearchParamType.DATE);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getValue() {
        return this.value;
    }

    @Override
    public void setValue(Date value) {
        this.value = value;
    }
}
