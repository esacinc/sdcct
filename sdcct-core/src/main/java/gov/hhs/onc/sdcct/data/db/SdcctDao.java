package gov.hhs.onc.sdcct.data.db;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriterion;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface SdcctDao<T extends SdcctEntity> extends SdcctEntityAccessor<T> {
    public boolean removeByEntityId(@Nonnegative long entityId) throws Exception;

    @Nonnegative
    public long remove(SdcctCriteria<T> criteria) throws Exception;

    public T save(T entity) throws Exception;

    public List<T> findAll(SdcctCriteria<T> criteria) throws Exception;

    @Nullable
    public T findByEntityId(@Nonnegative long entityId) throws Exception;

    @Nullable
    public T find(SdcctCriteria<T> criteria) throws Exception;

    public boolean existsByEntityId(@Nonnegative long entityId) throws Exception;

    public boolean exists(SdcctCriteria<T> criteria) throws Exception;

    @Nonnegative
    public long count(SdcctCriteria<T> criteria) throws Exception;
    
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<T> buildCriteria(SdcctCriterion<T>... criterions);
}
