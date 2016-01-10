package gov.hhs.onc.sdcct.beans;

import javax.annotation.Nullable;

public interface NamedBean extends IdentifiedBean {
    public default boolean isSetName() {
        return (this.getName() != null);
    }

    @Nullable
    public String getName();

    public void setName(@Nullable String name);
}
