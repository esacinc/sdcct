package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;

public interface SdcctDao<T extends SdcctEntity> extends SdcctDataAccessor<T, T> {
}
