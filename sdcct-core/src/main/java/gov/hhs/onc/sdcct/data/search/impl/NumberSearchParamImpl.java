package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "searchParamNum")
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
    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
