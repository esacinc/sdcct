package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.impl.SdcctCriteria;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.hibernate.criterion.Criterion;

public interface SdcctDataAccessor<T, U extends SdcctEntity> extends SdcctEntityAccessor<T, U> {
    public boolean remove(T bean) throws Exception;

    public boolean removeByNaturalId(Serializable naturalId) throws Exception;

    public boolean removeById(Serializable id) throws Exception;

    @Nonnegative
    public long remove(SdcctCriteria<U> criteria) throws Exception;

    @Nonnegative
    public long save(T bean) throws Exception;

    public List<T> findAll(SdcctCriteria<U> criteria) throws Exception;

    @Nullable
    public T findByNaturalId(Serializable naturalId) throws Exception;

    @Nullable
    public T findById(Serializable id) throws Exception;

    @Nullable
    public T find(SdcctCriteria<U> criteria) throws Exception;

    public boolean existsByNaturalId(Serializable naturalId) throws Exception;

    public boolean existsById(Serializable id) throws Exception;

    public boolean exists(T bean) throws Exception;

    public boolean exists(SdcctCriteria<U> criteria) throws Exception;

    @Nonnegative
    public long count(SdcctCriteria<U> criteria) throws Exception;

    public void reindex() throws Exception;

    public SdcctCriteria<U> buildCriteria(Criterion ... criterions);
}
