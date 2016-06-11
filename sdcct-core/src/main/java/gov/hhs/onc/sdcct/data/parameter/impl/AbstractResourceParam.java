package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbSequenceNames;
import gov.hhs.onc.sdcct.data.impl.AbstractSdcctEntity;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParam;
import java.io.Serializable;
import javax.annotation.Nonnegative;
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
@Entity(name = "resourceParam")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractResourceParam<T extends Serializable> extends AbstractSdcctEntity implements ResourceParam<T> {
    protected SdcctResource resource;
    protected ResourceParamType type;
    protected Boolean meta;
    protected String name;
    protected T value;

    private final static long serialVersionUID = 0L;

    protected AbstractResourceParam(@Nullable SdcctResource resource, ResourceParamType type, Boolean meta, String name, T value) {
        this(type);

        this.resource = resource;
        this.meta = meta;
        this.name = name;
        this.value = value;
    }

    protected AbstractResourceParam(ResourceParamType type) {
        this.type = type;
    }

    @Column(name = DbColumnNames.ENTITY_ID)
    @GeneratedValue(generator = DbSequenceNames.RESOURCE_PARAM_ENTITY_ID, strategy = GenerationType.SEQUENCE)
    @Id
    @Nonnegative
    @Nullable
    @Override
    @SequenceGenerator(allocationSize = 1, name = DbSequenceNames.RESOURCE_PARAM_ENTITY_ID, sequenceName = DbSequenceNames.RESOURCE_PARAM_ENTITY_ID)
    public Long getEntityId() {
        return super.getEntityId();
    }

    @Column(name = DbColumnNames.META, nullable = false, updatable = false)
    @Override
    public Boolean isMeta() {
        return this.meta;
    }

    @Override
    public void setMeta(Boolean meta) {
        this.meta = meta;
    }

    @Column(name = DbColumnNames.NAME, nullable = false, updatable = false)
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
    public ResourceParamType getType() {
        return this.type;
    }

    @Override
    public void setType(ResourceParamType type) {
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
