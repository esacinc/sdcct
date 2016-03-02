package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@Analyzer(impl = StandardAnalyzer.class)
@Embeddable
@Entity(name = "searchParamDate")
@Indexed(index = DbTableNames.SEARCH_PARAM_DATE)
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
    @Field(name = DbColumnNames.VALUE, store = Store.YES)
    @Override
    @SortableField(forField = DbColumnNames.VALUE)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getValue() {
        return this.value;
    }

    @Override
    public void setValue(Date value) {
        this.value = value;
    }
}
