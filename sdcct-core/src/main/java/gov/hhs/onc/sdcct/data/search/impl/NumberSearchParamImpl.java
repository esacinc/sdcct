package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;

@Analyzer(impl = StandardAnalyzer.class)
@Embeddable
@Entity(name = "searchParamNum")
@Indexed(index = DbTableNames.SEARCH_PARAM_NUM)
@Table(name = DbTableNames.SEARCH_PARAM_NUM)
public class NumberSearchParamImpl extends AbstractSearchParam implements NumberSearchParam {
    private BigDecimal value;

    public NumberSearchParamImpl(@Nullable Long resourceEntityId, String name, BigDecimal value) {
        super(SearchParamType.NUMBER, resourceEntityId, name);

        this.value = value;
    }

    public NumberSearchParamImpl() {
        super(SearchParamType.NUMBER);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Field(name = DbColumnNames.VALUE, store = Store.YES)
    @NumericField
    @Override
    @SortableField(forField = DbColumnNames.VALUE)
    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
