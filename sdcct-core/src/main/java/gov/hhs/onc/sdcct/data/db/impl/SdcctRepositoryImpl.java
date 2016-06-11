package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriterion;
import gov.hhs.onc.sdcct.data.db.criteria.impl.SdcctCriteriaImpl;
import gov.hhs.onc.sdcct.data.db.logging.impl.LoggingIndexQueryInterceptor;
import gov.hhs.onc.sdcct.data.db.metadata.EntityMetadata;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.hibernate.search.Search;
import org.springframework.transaction.annotation.Transactional;

public class SdcctRepositoryImpl<T extends SdcctEntity> extends AbstractSdcctEntityAccessor<T> implements SdcctRepository<T> {
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    private EntityMetadata entityMetadata;
    private LoggingIndexQueryInterceptor indexQueryInterceptor;

    public SdcctRepositoryImpl(EntityManager entityManager, Class<T> entityClass, Class<? extends T> entityImplClass, EntityMetadata entityMetadata) {
        super(entityClass, entityImplClass);

        this.criteriaBuilder = (this.entityManager = entityManager).getCriteriaBuilder();
        this.indexQueryInterceptor = new LoggingIndexQueryInterceptor(this.entityImplClass, (this.entityMetadata = entityMetadata).getName());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional
    public boolean removeByEntityId(@Nonnegative long entityId) throws Exception {
        return (this.remove(this.buildCriteria((builder, query, root) -> builder.equal(root.get(DbPropertyNames.ENTITY_ID), entityId))) > 0);
    }

    @Nonnegative
    @Override
    @Transactional
    public long remove(SdcctCriteria<T> criteria) throws Exception {
        return criteria.delete(this.entityManager);
    }

    @Override
    @Transactional
    public T save(T entity) throws Exception {
        Session session = this.buildSession();
        long entityId;

        // noinspection ConstantConditions
        if (entity.hasEntityId()) {
            session.update(entity);

            // noinspection ConstantConditions
            entityId = entity.getEntityId();
        } else {
            entityId = ((long) session.save(entity));
        }

        return this.findByEntityId(entityId);
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
    public T findByEntityId(@Nonnegative long entityId) throws Exception {
        return this.find(this.buildCriteria((builder, query, root) -> builder.equal(root.get(DbPropertyNames.ENTITY_ID), entityId)));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public T find(SdcctCriteria<T> criteria) throws Exception {
        return criteria.first(this.entityManager);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional(readOnly = true)
    public boolean existsByEntityId(@Nonnegative long entityId) throws Exception {
        return this.exists(this.buildCriteria((builder, query, root) -> builder.equal(root.get(DbPropertyNames.ENTITY_ID), entityId)));
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
}
