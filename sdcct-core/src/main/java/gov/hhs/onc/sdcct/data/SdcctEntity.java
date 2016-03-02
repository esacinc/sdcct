package gov.hhs.onc.sdcct.data;

import javax.annotation.Nullable;

public interface SdcctEntity {
    public boolean hasEntityId();

    @Nullable
    public Long getEntityId();

    public void setEntityId(@Nullable Long entityId);
}
