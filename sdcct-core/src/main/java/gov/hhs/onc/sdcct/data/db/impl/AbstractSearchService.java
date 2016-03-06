package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.data.search.impl.SearchParamMetadata;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSearchService<T, U extends ResourceEntity, V extends SdcctDao<U>, W extends SdcctDataService<U, V>, X extends SdcctRegistry<T, U, V, W>>
    extends AbstractSdcctEntityAccessor<T, U> implements SearchService<T, U, V, W, X> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected X registry;

    protected EntityMetadata entityMetadata;
    protected Map<String, SearchParamMetadata> entitySearchParamMetadatas;

    protected AbstractSearchService(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass) {
        super(beanClass, beanImplClass, entityClass, entityImplClass);
    }

    @Override
    public List<T> search(MultivaluedMap<String, String> params) throws Exception {
        // TODO: implement

        return Collections.emptyList();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.entitySearchParamMetadatas = (this.entityMetadata = this.registry.getEntityMetadata()).getSearchParams();
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.entityMetadata;
    }
}
