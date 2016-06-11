package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceAccessor;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public interface SearchService<T, U extends ResourceMetadata<T>, V extends SdcctResource, W extends SdcctResourceRegistry<T, U, V>>
    extends SdcctResourceAccessor<T, U, V> {
    public List<T> search(MultivaluedMap<String, String> params) throws Exception;
}
