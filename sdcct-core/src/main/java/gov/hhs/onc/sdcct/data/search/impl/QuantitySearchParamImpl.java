package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "searchParamQuantity")
@Table(name = DbTableNames.SEARCH_PARAM_QUANTITY)
public class QuantitySearchParamImpl extends AbstractCodeSearchParam implements QuantitySearchParam {
    private String units;
    private BigDecimal value;

    public QuantitySearchParamImpl(@Nullable Long resourceEntityId, String name, String codeSystem, String units, BigDecimal value) {
        super(SearchParamType.QUANTITY, resourceEntityId, name, codeSystem);

        this.units = units;
        this.value = value;
    }

    public QuantitySearchParamImpl() {
        super(SearchParamType.QUANTITY);
    }

    @Override
    public boolean hasUnits() {
        return (this.units != null);
    }

    @Column(name = DbColumnNames.UNITS)
    @Nullable
    @Override
    public String getUnits() {
        return this.units;
    }

    @Override
    public void setUnits(@Nullable String units) {
        this.units = units;
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
