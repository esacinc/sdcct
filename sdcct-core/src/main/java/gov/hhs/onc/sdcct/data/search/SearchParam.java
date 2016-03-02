package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.SdcctEntity;

public interface SearchParam extends NamedBean, SdcctEntity {
    public void setName(String name);

    public Long getResourceEntityId();

    public void setResourceEntityId(Long resourceEntityId);

    public SearchParamType getType();
}
