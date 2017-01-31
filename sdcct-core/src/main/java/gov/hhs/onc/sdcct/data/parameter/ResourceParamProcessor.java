package gov.hhs.onc.sdcct.data.parameter;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceAccessor;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import org.hibernate.HibernateException;

public interface ResourceParamProcessor<T, U extends ResourceMetadata<?>, V extends SdcctResource> extends SdcctResourceAccessor<T, V> {
    public void process(XdmDocument contentDoc, V entity) throws HibernateException;
}
