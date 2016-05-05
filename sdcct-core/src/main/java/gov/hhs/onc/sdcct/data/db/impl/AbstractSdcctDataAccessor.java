package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDataAccessor;

public abstract class AbstractSdcctDataAccessor<T, U extends SdcctEntity> extends AbstractSdcctEntityAccessor<U> implements SdcctDataAccessor<T, U> {
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractSdcctDataAccessor(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass) {
        super(entityClass, entityImplClass);

        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }
}
