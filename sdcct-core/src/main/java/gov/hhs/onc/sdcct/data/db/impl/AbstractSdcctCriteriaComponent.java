package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import javax.annotation.Nullable;

public abstract class AbstractSdcctCriteriaComponent<T extends AbstractSdcctCriteriaComponent<T>> {
    protected Class<? extends SdcctEntity> entityClass;
    protected String propName;

    protected AbstractSdcctCriteriaComponent(@Nullable Class<? extends SdcctEntity> entityClass, String propName) {
        this.entityClass = entityClass;
        this.propName = propName;
    }

    public boolean hasEntityClass() {
        return (this.entityClass != null);
    }

    @Nullable
    public Class<? extends SdcctEntity> getEntityClass() {
        return this.entityClass;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setEntityClass(@Nullable Class<? extends SdcctEntity> entityClass) {
        this.entityClass = entityClass;

        return ((T) this);
    }

    public String getPropertyName() {
        return this.propName;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T setPropertyName(String propName) {
        this.propName = propName;

        return ((T) this);
    }
}
