package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractSdcctDataService<T extends SdcctEntity, U extends SdcctDao<T>> extends AbstractSdcctDataAccessor<T, T> implements
    SdcctDataService<T, U> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected U dao;

    protected AbstractSdcctDataService(Class<T> entityClass, Class<? extends T> entityImplClass) {
        super(entityClass, entityImplClass, entityClass, entityImplClass);
    }

    @Override
    @Transactional
    public boolean remove(T bean) throws Exception {
        return this.dao.remove(bean);
    }

    @Override
    @Transactional
    public boolean removeByNaturalId(Serializable naturalId) throws Exception {
        return this.dao.removeByNaturalId(naturalId);
    }

    @Override
    @Transactional
    public boolean removeById(Serializable id) throws Exception {
        return this.dao.removeById(id);
    }

    @Nonnegative
    @Override
    @Transactional
    public long remove(SdcctCriteria<T> criteria) throws Exception {
        return this.dao.remove(criteria);
    }

    @Nonnegative
    @Override
    @Transactional
    public long save(T bean) throws Exception {
        return this.dao.save(bean);
    }

    @Override
    public List<T> findAll(SdcctCriteria<T> criteria) throws Exception {
        return this.dao.findAll(criteria);
    }

    @Nullable
    @Override
    public T findByNaturalId(Serializable naturalId) throws Exception {
        return this.dao.findByNaturalId(naturalId);
    }

    @Nullable
    @Override
    public T findById(Serializable id) throws Exception {
        return this.dao.findById(id);
    }

    @Nullable
    @Override
    public T find(SdcctCriteria<T> criteria) throws Exception {
        return this.dao.find(criteria);
    }

    @Override
    public boolean exists(T bean) throws Exception {
        return this.dao.exists(bean);
    }

    @Override
    public boolean existsByNaturalId(Serializable naturalId) throws Exception {
        return this.dao.existsByNaturalId(naturalId);
    }

    @Override
    public boolean existsById(Serializable id) throws Exception {
        return this.dao.existsById(id);
    }

    @Override
    public boolean exists(SdcctCriteria<T> criteria) throws Exception {
        return this.dao.exists(criteria);
    }

    @Nonnegative
    @Override
    public long count(SdcctCriteria<T> criteria) throws Exception {
        return this.dao.count(criteria);
    }

    @Override
    @Transactional
    public void reindex() throws Exception {
        this.dao.reindex();
    }

    @Override
    public SdcctCriteria<T> buildCriteria(Criterion ... criterions) {
        return this.dao.buildCriteria(criterions);
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.dao.getEntityMetadata();
    }
}
