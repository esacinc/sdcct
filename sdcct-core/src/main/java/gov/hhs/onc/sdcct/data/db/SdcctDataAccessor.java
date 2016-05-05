package gov.hhs.onc.sdcct.data.db;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface SdcctDataAccessor<T, U extends SdcctEntity> extends SdcctEntityAccessor<U> {
    public boolean remove(T bean) throws Exception;

    public boolean removeById(long id) throws Exception;

    @Nonnegative
    public long remove(SdcctCriteria<U> criteria) throws Exception;

    @Nonnegative
    public long save(T bean) throws Exception;

    public List<T> findAll(SdcctCriteria<U> criteria) throws Exception;

    @Nullable
    public T findById(long id) throws Exception;

    @Nullable
    public T find(SdcctCriteria<U> criteria) throws Exception;

    public boolean exists(T bean) throws Exception;

    public boolean existsById(long id) throws Exception;

    public boolean exists(SdcctCriteria<U> criteria) throws Exception;

    @Nonnegative
    public long count(SdcctCriteria<U> criteria) throws Exception;

    public void reindex() throws Exception;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<U> buildCriteria(SdcctCriterion<U> ... criterions);

    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();
}
