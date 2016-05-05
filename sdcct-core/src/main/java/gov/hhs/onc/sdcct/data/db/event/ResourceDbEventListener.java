package gov.hhs.onc.sdcct.data.db.event;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.ResourceProcessor;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;

public interface ResourceDbEventListener<T extends Enum<T> & IdentifiedBean, U, V extends SdcctResource, W extends ResourceMetadata<T, ? extends U>, X extends ResourceProcessor<T, U, V, W>>
    extends DbEventListener<V> {
}
