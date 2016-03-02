package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDataAccessor;

public abstract class AbstractSdcctDataAccessor<T, U extends SdcctEntity> implements SdcctDataAccessor<T, U> {
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;
    protected Class<U> entityClass;
    protected Class<? extends U> entityImplClass;

    protected AbstractSdcctDataAccessor(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass) {
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
        this.entityClass = entityClass;
        this.entityImplClass = entityImplClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }

    @Override
    public Class<U> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public Class<? extends U> getEntityImplClass() {
        return this.entityImplClass;
    }
}
