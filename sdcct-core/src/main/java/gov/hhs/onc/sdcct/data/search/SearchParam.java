package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.SdcctResource;
import java.io.Serializable;

public interface SearchParam<T extends Serializable> extends NamedBean, SdcctEntity {
    public void setName(String name);
    
    public SdcctResource getResource();

    public void setResource(SdcctResource resource);

    public SearchParamType getType();

    public void setType(SearchParamType type);

    public T getValue();

    public void setValue(T value);
}
