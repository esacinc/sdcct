package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.impl.AbstractSdcctEntity;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import org.hibernate.search.annotations.DocumentId;

@MappedSuperclass
public abstract class AbstractSearchParam extends AbstractSdcctEntity implements SearchParam {
    protected String name;
    protected Long resourceEntityId;
    protected SearchParamType type;

    protected AbstractSearchParam(SearchParamType type, @Nullable Long resourceEntityId, String name) {
        this(type);

        this.resourceEntityId = resourceEntityId;
        this.name = name;
    }

    protected AbstractSearchParam(SearchParamType type) {
        this.type = type;
    }

    @Column(name = DbColumnNames.ENTITY_ID)
    @DocumentId(name = DbColumnNames.ENTITY_ID)
    @GeneratedValue(generator = DbSequenceNames.SEARCH_PARAM_ENTITY_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.SEARCH_PARAM_ENTITY_ID, sequenceName = DbSequenceNames.SEARCH_PARAM_ENTITY_ID)
    public Long getEntityId() {
        return super.getEntityId();
    }
    
    @Column(name = DbColumnNames.NAME, nullable = false)
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @Override
    public Long getResourceEntityId() {
        return this.resourceEntityId;
    }

    @Override
    public void setResourceEntityId(Long resourceEntityId) {
        this.resourceEntityId = resourceEntityId;
    }

    @Override
    @Transient
    public SearchParamType getType() {
        return this.type;
    }
}
