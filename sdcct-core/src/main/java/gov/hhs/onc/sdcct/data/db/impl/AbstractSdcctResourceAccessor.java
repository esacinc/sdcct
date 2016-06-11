package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceAccessor;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;

public abstract class AbstractSdcctResourceAccessor<T, U extends ResourceMetadata<?>, V extends SdcctResource> extends AbstractSdcctEntityAccessor<V>
    implements SdcctResourceAccessor<T, U, V> {
    protected SpecificationType specType;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;

    protected AbstractSdcctResourceAccessor(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, Class<V> entityClass,
        Class<? extends V> entityImplClass) {
        super(entityClass, entityImplClass);

        this.specType = specType;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
