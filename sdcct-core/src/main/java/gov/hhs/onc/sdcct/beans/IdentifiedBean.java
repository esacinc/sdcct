package gov.hhs.onc.sdcct.beans;

import javax.annotation.Nullable;

public interface IdentifiedBean {
    public default boolean isSetId() {
        return (this.getId() != null);
    }

    @Nullable
    public String getId();

    public void setId(@Nullable String id);
}
