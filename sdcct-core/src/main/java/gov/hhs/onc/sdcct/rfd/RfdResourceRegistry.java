package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;

public interface RfdResourceRegistry<T, U extends RfdResource, V extends SdcctDao<U>, W extends SdcctDataService<U, V>> extends SdcctRegistry<T, U, V, W> {
}
