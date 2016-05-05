package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.impl.AbstractSdcctEntity;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.io.Serializable;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

@DiscriminatorColumn(name = DbColumnNames.TYPE)
@Entity(name = "searchParam")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractSearchParam<T extends Serializable> extends AbstractSdcctEntity implements SearchParam<T> {
    protected String name;
    protected SearchParamType type;
    protected SdcctResource resource;
    protected T value;

    private final static long serialVersionUID = 0L;

    protected AbstractSearchParam(SearchParamType type, @Nullable SdcctResource resource, String name, T value) {
        this(type);

        this.resource = resource;
        this.name = name;
        this.value = value;
    }

    protected AbstractSearchParam(SearchParamType type) {
        this.type = type;
    }

    @Column(name = DbColumnNames.ID)
    @GeneratedValue(generator = DbSequenceNames.SEARCH_PARAM_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.SEARCH_PARAM_ID, sequenceName = DbSequenceNames.SEARCH_PARAM_ID)
    public Long getId() {
        return super.getId();
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

    @Override
    @Transient
    public SdcctResource getResource() {
        return this.resource;
    }

    @Override
    public void setResource(SdcctResource resource) {
        this.resource = resource;
    }

    @Column(name = DbColumnNames.TYPE, nullable = false, updatable = false)
    @Override
    public SearchParamType getType() {
        return this.type;
    }

    @Override
    public void setType(SearchParamType type) {
        this.type = type;
    }

    @Override
    @Transient
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
