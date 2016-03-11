package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_REF)
@Entity(name = "searchParamRef")
@Table(name = DbTableNames.SEARCH_PARAM_REF)
public class RefSearchParamImpl extends AbstractSearchParam<String> implements RefSearchParam {
    public RefSearchParamImpl(@Nullable ResourceEntity resource, String name, String value) {
        super(SearchParamType.REF, resource, name, value);
    }

    public RefSearchParamImpl() {
        super(SearchParamType.REF);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public String getValue() {
        return super.getValue();
    }
}
