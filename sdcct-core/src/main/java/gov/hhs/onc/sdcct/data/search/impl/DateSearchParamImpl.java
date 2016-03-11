package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_DATE)
@Entity(name = "searchParamDate")
@Table(name = DbTableNames.SEARCH_PARAM_DATE)
public class DateSearchParamImpl extends AbstractSearchParam<Date> implements DateSearchParam {
    public DateSearchParamImpl(@Nullable ResourceEntity resource, String name, Date value) {
        super(SearchParamType.DATE, resource, name, value);
    }

    public DateSearchParamImpl() {
        super(SearchParamType.DATE);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getValue() {
        return super.getValue();
    }
}
