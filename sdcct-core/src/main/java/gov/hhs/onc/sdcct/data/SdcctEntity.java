package gov.hhs.onc.sdcct.data;

import java.io.Serializable;
import javax.annotation.Nullable;

public interface SdcctEntity extends Serializable {
    public boolean hasId();

    @Nullable
    public Long getId();

    public void setId(@Nullable Long id);
}
