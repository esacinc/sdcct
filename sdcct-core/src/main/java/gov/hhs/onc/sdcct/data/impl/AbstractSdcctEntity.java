package gov.hhs.onc.sdcct.data.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import javax.annotation.Nullable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.annotations.Proxy;

@Access(AccessType.PROPERTY)
@MappedSuperclass
@Proxy(lazy = false)
public abstract class AbstractSdcctEntity implements SdcctEntity {
    protected Long entityId;

    @Override
    public boolean hasEntityId() {
        return (this.entityId != null);
    }

    @Nullable
    @Override
    @Transient
    public Long getEntityId() {
        return this.entityId;
    }

    @Override
    public void setEntityId(@Nullable Long entityId) {
        this.entityId = entityId;
    }
}
