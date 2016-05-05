package gov.hhs.onc.sdcct.data.db.event;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import org.hibernate.event.spi.SaveOrUpdateEventListener;

public interface DbEventListener<T extends SdcctEntity> extends SaveOrUpdateEventListener {
}
