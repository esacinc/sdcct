package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceAccessor;

public abstract class AbstractSdcctResourceAccessor<T, U extends SdcctResource> extends AbstractSdcctEntityAccessor<U> implements SdcctResourceAccessor<T, U> {
    protected SpecificationType specType;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractSdcctResourceAccessor(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass,
        Class<? extends U> entityImplClass) {
        super(entityClass, entityImplClass);

        this.specType = specType;
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

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
