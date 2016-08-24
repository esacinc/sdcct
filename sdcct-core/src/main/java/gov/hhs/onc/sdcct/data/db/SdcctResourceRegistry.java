package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface SdcctResourceRegistry<T, U extends ResourceMetadata<T>, V extends SdcctResource> extends SdcctDao<V>, SdcctResourceAccessor<T, V> {
    public V saveBean(T bean) throws Exception;

    public V saveBean(T bean, V entity) throws Exception;

    public List<T> findAllBeans(SdcctCriteria<V> criteria) throws Exception;

    @Nullable
    public T findBeanByEntityId(@Nonnegative long entityId) throws Exception;

    @Nullable
    public T findBean(SdcctCriteria<V> criteria) throws Exception;
}
