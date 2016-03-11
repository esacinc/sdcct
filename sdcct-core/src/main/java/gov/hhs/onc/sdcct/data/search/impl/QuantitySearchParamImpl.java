package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.math.BigDecimal;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_QUANTITY)
@Entity(name = "searchParamQuantity")
@Table(name = DbTableNames.SEARCH_PARAM_QUANTITY)
public class QuantitySearchParamImpl extends AbstractCodeSearchParam<BigDecimal> implements QuantitySearchParam {
    private String units;

    public QuantitySearchParamImpl(@Nullable ResourceEntity resource, String name, @Nullable URI codeSystemUri, @Nullable String units, BigDecimal value) {
        super(SearchParamType.QUANTITY, resource, name, codeSystemUri, value);

        this.units = units;
    }

    public QuantitySearchParamImpl() {
        super(SearchParamType.QUANTITY);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
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
        return super.getValue();
    }
}
