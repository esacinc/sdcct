package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctEntityAccessor<T extends SdcctEntity> extends InitializingBean {
    public Class<T> getEntityClass();

    public Class<? extends T> getEntityImplClass();
}
