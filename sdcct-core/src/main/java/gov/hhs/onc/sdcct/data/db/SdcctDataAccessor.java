package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.impl.SdcctCriteria;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.springframework.beans.factory.InitializingBean;

public interface SdcctDataAccessor<T, U extends SdcctEntity> extends InitializingBean {
    public boolean remove(T bean) throws Exception;

    public boolean removeByNaturalId(Serializable naturalId) throws Exception;

    public boolean removeById(Serializable id) throws Exception;

    @Nonnegative
    public long remove(SdcctCriteria criteria) throws Exception;

    @Nonnegative
    public long save(T bean) throws Exception;

    public List<T> findAll(SdcctCriteria criteria) throws Exception;

    @Nullable
    public T findByNaturalId(Serializable naturalId) throws Exception;

    @Nullable
    public T findById(Serializable id) throws Exception;

    @Nullable
    public T find(SdcctCriteria criteria) throws Exception;

    public boolean existsByNaturalId(Serializable naturalId) throws Exception;

    public boolean existsById(Serializable id) throws Exception;

    public boolean exists(T bean) throws Exception;

    public boolean exists(SdcctCriteria criteria) throws Exception;

    @Nonnegative
    public long count(SdcctCriteria criteria) throws Exception;

    public void reindex() throws Exception;

    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public Class<U> getEntityClass();

    public Class<? extends U> getEntityImplClass();
}
