package gov.hhs.onc.sdcct.data.parameter;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.SdcctResource;
import java.io.Serializable;

public interface ResourceParam<T extends Serializable> extends NamedBean, SdcctEntity {
    public Boolean isMeta();

    public void setMeta(Boolean meta);

    public void setName(String name);

    public SdcctResource getResource();

    public void setResource(SdcctResource resource);

    public ResourceParamType getType();

    public void setType(ResourceParamType type);

    public T getValue();

    public void setValue(T value);
}
