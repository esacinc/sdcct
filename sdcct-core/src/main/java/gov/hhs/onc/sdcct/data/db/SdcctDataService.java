package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;

public interface SdcctDataService<T extends SdcctEntity, U extends SdcctDao<T>> extends SdcctDataAccessor<T, T> {
}
