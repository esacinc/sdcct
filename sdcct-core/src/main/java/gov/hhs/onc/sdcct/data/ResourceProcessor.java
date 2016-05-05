package gov.hhs.onc.sdcct.data;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import org.hibernate.HibernateException;

public interface ResourceProcessor<T extends Enum<T> & IdentifiedBean, U, V extends SdcctResource, W extends ResourceMetadata<T, ? extends U>> {
    public void process(V resource, T type) throws HibernateException;
}
