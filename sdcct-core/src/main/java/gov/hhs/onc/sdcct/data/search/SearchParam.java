package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.SdcctEntity;

public interface SearchParam<T> extends NamedBean, SdcctEntity {
    public void setName(String name);

    public ResourceEntity getResource();

    public void setResource(ResourceEntity resource);

    public SearchParamType getType();

    public void setType(SearchParamType type);

    public T getValue();

    public void setValue(T value);
}
