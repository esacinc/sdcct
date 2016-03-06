package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDataAccessor;

public abstract class AbstractSdcctDataAccessor<T, U extends SdcctEntity> extends AbstractSdcctEntityAccessor<T, U> implements SdcctDataAccessor<T, U> {
    protected AbstractSdcctDataAccessor(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass) {
        super(beanClass, beanImplClass, entityClass, entityImplClass);
    }
}
