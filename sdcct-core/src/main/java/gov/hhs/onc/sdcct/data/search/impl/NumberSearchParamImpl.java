package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_NUM)
@Entity(name = "searchParamNum")
@Table(name = DbTableNames.SEARCH_PARAM_NUM)
public class NumberSearchParamImpl extends AbstractSearchParam<BigDecimal> implements NumberSearchParam {
    public NumberSearchParamImpl(@Nullable ResourceEntity resource, String name, BigDecimal value) {
        super(SearchParamType.NUM, resource, name, value);
    }

    public NumberSearchParamImpl() {
        super(SearchParamType.NUM);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public BigDecimal getValue() {
        return super.getValue();
    }
}
