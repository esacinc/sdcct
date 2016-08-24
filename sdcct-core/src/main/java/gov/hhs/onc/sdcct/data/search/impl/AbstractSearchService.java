package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctResourceAccessor;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.search.SearchService;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public abstract class AbstractSearchService<T, U extends ResourceMetadata<T>, V extends SdcctResource, W extends SdcctResourceRegistry<T, U, V>>
    extends AbstractSdcctResourceAccessor<T, V> implements SearchService<T, U, V, W> {
    protected U resourceMetadata;
    protected W resourceRegistry;

    protected AbstractSearchService(U resourceMetadata, W resourceRegistry) {
        super(resourceMetadata.getSpecificationType(), resourceMetadata.getBeanClass(), resourceMetadata.getBeanImplClass(), resourceRegistry.getEntityClass(),
            resourceRegistry.getEntityImplClass());

        this.resourceMetadata = resourceMetadata;
        this.resourceRegistry = resourceRegistry;
    }

    @Override
    public List<T> search(MultivaluedMap<String, String> params) throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
