package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.impl.EntityMetadata;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctEntityAccessor<T, U extends SdcctEntity> extends InitializingBean {
    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public Class<U> getEntityClass();

    public Class<? extends U> getEntityImplClass();

    public EntityMetadata getEntityMetadata();
}
