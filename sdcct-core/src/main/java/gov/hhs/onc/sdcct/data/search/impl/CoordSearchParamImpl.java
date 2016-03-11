package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.CoordEntity;
import gov.hhs.onc.sdcct.data.search.CoordSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Target;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_COORD)
@Entity(name = "searchParamCoord")
@Table(name = DbTableNames.SEARCH_PARAM_COORD)
public class CoordSearchParamImpl extends AbstractSearchParam<CoordEntity> implements CoordSearchParam {
    public CoordSearchParamImpl(@Nullable ResourceEntity resource, String name, CoordEntity value) {
        super(SearchParamType.COORD, resource, name, value);
    }

    public CoordSearchParamImpl() {
        super(SearchParamType.COORD);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Embedded
    @Override
    @Target(CoordEntityImpl.class)
    public CoordEntity getValue() {
        return super.getValue();
    }
}
