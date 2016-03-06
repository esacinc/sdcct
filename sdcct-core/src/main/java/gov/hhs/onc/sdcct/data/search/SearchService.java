package gov.hhs.onc.sdcct.data.search;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctEntityAccessor;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public interface SearchService<T, U extends ResourceEntity, V extends SdcctDao<U>, W extends SdcctDataService<U, V>, X extends SdcctRegistry<T, U, V, W>>
    extends SdcctEntityAccessor<T, U> {
    public List<T> search(MultivaluedMap<String, String> params) throws Exception;
}
