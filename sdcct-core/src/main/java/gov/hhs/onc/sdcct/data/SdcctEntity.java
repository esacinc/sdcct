package gov.hhs.onc.sdcct.data;

import java.io.Serializable;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface SdcctEntity extends Serializable {
    public boolean hasEntityId();

    @Nonnegative
    @Nullable
    public Long getEntityId();

    public void setEntityId(@Nonnegative @Nullable Long entityId);
}
