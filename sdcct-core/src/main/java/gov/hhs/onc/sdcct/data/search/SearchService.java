package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctEntityAccessor;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public interface SearchService<T, U extends SdcctResource, V extends SdcctRegistry<T, U>> extends SdcctEntityAccessor<U> {
    public List<T> search(MultivaluedMap<String, String> params) throws Exception;
}
