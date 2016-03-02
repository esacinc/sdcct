package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.ResourceEntity;

public interface SdcctRegistry<T, U extends ResourceEntity, V extends SdcctDao<U>, W extends SdcctDataService<U, V>> extends SdcctDataAccessor<T, U> {
}
