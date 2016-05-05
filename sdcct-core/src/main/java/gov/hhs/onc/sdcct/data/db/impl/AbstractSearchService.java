package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.data.metadata.EntityMetadata;
import gov.hhs.onc.sdcct.data.search.SearchService;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;

public abstract class AbstractSearchService<T, U extends SdcctResource, V extends SdcctRegistry<T, U>> extends AbstractSdcctEntityAccessor<U> implements
    SearchService<T, U, V> {
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;
    protected V registry;
    protected EntityMetadata entityMetadata;

    protected AbstractSearchService(Class<T> beanClass, Class<? extends T> beanImplClass, Class<U> entityClass, Class<? extends U> entityImplClass, V registry) {
        super(entityClass, entityImplClass);

        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
        this.registry = registry;
    }

    @Override
    public List<T> search(MultivaluedMap<String, String> params) throws Exception {
        return Collections.emptyList();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.entityMetadata;
    }
}
