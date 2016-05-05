package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctEntityAccessor;

public abstract class AbstractSdcctEntityAccessor<T extends SdcctEntity> implements SdcctEntityAccessor<T> {
    protected Class<T> entityClass;
    protected Class<? extends T> entityImplClass;

    protected AbstractSdcctEntityAccessor(Class<T> entityClass, Class<? extends T> entityImplClass) {
        this.entityClass = entityClass;
        this.entityImplClass = entityImplClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public Class<? extends T> getEntityImplClass() {
        return this.entityImplClass;
    }
}
