package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.metadata.EntityMetadata;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctEntityAccessor<T extends SdcctEntity> extends InitializingBean {
    public Class<T> getEntityClass();

    public Class<? extends T> getEntityImplClass();

    public EntityMetadata getEntityMetadata();
}
