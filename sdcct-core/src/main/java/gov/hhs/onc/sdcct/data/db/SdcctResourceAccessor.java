package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.SdcctResourceDescriptor;

public interface SdcctResourceAccessor<T, U extends SdcctResource> extends SdcctEntityAccessor<U>, SdcctResourceDescriptor<T>, SpecifiedBean {
}
