package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.SdcctEntityDescriptor;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctEntityAccessor<T extends SdcctEntity> extends InitializingBean, SdcctEntityDescriptor<T> {
}
