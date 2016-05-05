package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.SdcctCriterion;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.metadata.EntityMetadata;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.search.Search;
import org.springframework.transaction.annotation.Transactional;

public class SdcctRepositoryImpl<T extends SdcctEntity> extends AbstractSdcctDataAccessor<T, T> implements SdcctRepository<T> {
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    private EntityMetadata entityMetadata;
    private LoggingIndexQueryInterceptor indexQueryInterceptor;

    public SdcctRepositoryImpl(EntityManager entityManager, Class<T> entityClass, Class<? extends T> entityImplClass, EntityMetadata entityMetadata) {
        super(entityClass, entityImplClass, entityClass, entityImplClass);

        this.criteriaBuilder = (this.entityManager = entityManager).getCriteriaBuilder();
        this.indexQueryInterceptor = new LoggingIndexQueryInterceptor(this.entityImplClass, (this.entityMetadata = entityMetadata).getName());
    }

    @Override
    @Transactional
    public boolean remove(T bean) throws Exception {
        // noinspection ConstantConditions
        return (bean.hasId() && this.removeById(bean.getId()));
    }

    @Override
    @Transactional
    public boolean removeById(long id) throws Exception {
        T bean = this.findById(id);

        if (bean != null) {
            this.entityManager.remove(bean);

            return true;
        } else {
            return false;
        }
    }

    @Nonnegative
    @Override
    @Transactional
    public long remove(SdcctCriteria<T> criteria) throws Exception {
        List<T> beans = this.findAll(criteria);

        if (CollectionUtils.isEmpty(beans)) {
            return 0;
        }

        Session session = this.buildSession();

        beans.forEach(session::delete);

        return beans.size();
    }

    @Override
    @Transactional
    public long save(T bean) throws Exception {
        Session session = this.buildSession();

        // noinspection ConstantConditions
        if (bean.hasId()) {
            session.update(bean);
            
            // noinspection ConstantConditions
            return bean.getId();
        } else {
            return ((long) session.save(bean));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(SdcctCriteria<T> criteria) throws Exception {
        return criteria.list(this.entityManager);
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional(readOnly = true)
    public T findById(long id) throws Exception {
        return this.find(this.buildCriteria((root, query, builder) -> builder.equal(root.get(DbPropertyNames.ID), id)));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public T find(SdcctCriteria<T> criteria) throws Exception {
        return criteria.first(this.entityManager);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(T bean) throws Exception {
        // noinspection ConstantConditions
        return (bean.hasId() && this.existsById(bean.getId()));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional(readOnly = true)
    public boolean existsById(long id) throws Exception {
        return this.exists(this.buildCriteria((root, query, builder) -> builder.equal(root.get(DbPropertyNames.ID), id)));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(SdcctCriteria<T> criteria) throws Exception {
        return criteria.exists(this.entityManager);
    }

    @Nonnegative
    @Override
    @Transactional(readOnly = true)
    public long count(SdcctCriteria<T> criteria) throws Exception {
        return criteria.count(this.entityManager);
    }

    @Override
    @Transactional
    public void reindex() throws Exception {
        if (this.entityMetadata.isIndexed()) {
            Search.getFullTextSession(this.buildSession()).createIndexer(this.entityImplClass).startAndWait();
        }
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<T> buildCriteria(SdcctCriterion<T> ... criterions) {
        return new SdcctCriteriaImpl<>(this.criteriaBuilder, this.entityClass, this.entityImplClass, this.entityMetadata, this.indexQueryInterceptor,
            criterions);
    }

    private Session buildSession() {
        return this.entityManager.unwrap(Session.class);
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.entityMetadata;
    }
}
