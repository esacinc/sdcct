package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;

public interface SdcctResourceAccessor<T, U extends ResourceMetadata<?>, V extends SdcctResource> extends SdcctEntityAccessor<V>, SpecifiedBean {
    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();
}
