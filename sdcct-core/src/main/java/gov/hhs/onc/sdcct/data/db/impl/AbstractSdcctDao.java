package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.SessionImpl;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctDao<T extends SdcctEntity> extends AbstractSdcctDataAccessor<T, T> implements SdcctDao<T> {
    @Autowired
    protected SessionFactory sessionFactory;

    protected EntityMetadata entityMetadata;
    protected LoggingIndexQueryInterceptor indexQueryInterceptor;

    protected AbstractSdcctDao(Class<T> entityClass, Class<? extends T> entityImplClass) {
        super(entityClass, entityImplClass, entityClass, entityImplClass);
    }

    @Override
    public boolean remove(T bean) throws Exception {
        // noinspection ConstantConditions
        return (bean.hasEntityId() && this.removeById(bean.getEntityId()));
    }

    @Override
    public boolean removeByNaturalId(Serializable naturalId) throws Exception {
        if (this.existsByNaturalId(naturalId)) {
            // noinspection ConstantConditions
            this.buildSession().delete(this.buildRefByNaturalId(naturalId));

            return true;
        }

        return false;
    }

    @Override
    public boolean removeById(Serializable id) throws Exception {
        if (this.existsById(id)) {
            // noinspection ConstantConditions
            this.buildSession().delete(this.buildRefById(id));

            return true;
        }

        return false;
    }

    @Nonnegative
    @Override
    public long remove(SdcctCriteria<T> criteria) throws Exception {
        List<T> beans = this.findAll(criteria);

        if (CollectionUtils.isEmpty(beans)) {
            return 0;
        }

        Session session = this.buildSession();

        beans.forEach(session::delete);

        return beans.size();
    }

    @Nonnegative
    @Override
    public long save(T bean) throws Exception {
        long entityId;
        Session session = this.buildSession();

        // noinspection ConstantConditions
        if (bean.hasEntityId() && this.existsById((entityId = bean.getEntityId()))) {
            session.update(bean);
        } else {
            bean.setEntityId((entityId = ((Long) session.save(bean))));
        }

        return entityId;
    }

    @Override
    public List<T> findAll(SdcctCriteria<T> criteria) throws Exception {
        return criteria.setSession(this.buildSession()).listEntities();
    }

    @Nullable
    @Override
    public T findByNaturalId(Serializable naturalId) throws Exception {
        return this.buildNaturalIdLoadAccess().load(naturalId);
    }

    @Nullable
    @Override
    public T findById(Serializable id) throws Exception {
        return this.buildIdLoadAccess().load(id);
    }

    @Nullable
    @Override
    public T find(SdcctCriteria<T> criteria) throws Exception {
        return criteria.setSession(this.buildSession()).firstEntity();
    }

    @Override
    public boolean exists(T bean) throws Exception {
        return (bean.hasEntityId() && this.existsById(bean.getEntityId()));
    }

    @Override
    public boolean existsByNaturalId(Serializable naturalId) throws Exception {
        return (this.buildNaturalIdLoadAccess().getReference(naturalId) != null);
    }

    @Override
    public boolean existsById(Serializable id) throws Exception {
        return (this.buildIdLoadAccess().getReference(id) != null);
    }

    @Override
    public boolean exists(SdcctCriteria<T> criteria) throws Exception {
        return (this.count(criteria) > 0);
    }

    @Nonnegative
    @Override
    public long count(SdcctCriteria<T> criteria) throws Exception {
        return ((long) criteria.setProjection(Projections.rowCount()).setSession(this.buildSession()).uniqueResult());
    }

    @Override
    public void reindex() throws Exception {
        if (this.entityMetadata.isIndexed()) {
            Search.getFullTextSession(this.buildSession()).createIndexer(this.entityImplClass).startAndWait();
        }
    }

    @Override
    public SdcctCriteria<T> buildCriteria(Criterion ... criterions) {
        return new SdcctCriteria<>(this.entityClass, this.entityImplClass, this.entityMetadata, this.indexQueryInterceptor).addAll(criterions);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.indexQueryInterceptor =
            new LoggingIndexQueryInterceptor(this.entityImplClass,
                (this.entityMetadata =
                    this.sessionFactory.getSessionFactoryOptions().getServiceRegistry().getService(MetadataService.class).getEntities()
                        .get(this.entityImplClass)).getName());
    }

    @Nullable
    protected T buildRefByNaturalId(Serializable naturalId) {
        return this.buildNaturalIdLoadAccess().getReference(naturalId);
    }

    protected SimpleNaturalIdLoadAccess<? extends T> buildNaturalIdLoadAccess() {
        return this.buildSession().bySimpleNaturalId(this.entityImplClass);
    }

    @Nullable
    protected T buildRefById(Serializable id) {
        return this.buildIdLoadAccess().getReference(id);
    }

    protected IdentifierLoadAccess<? extends T> buildIdLoadAccess() {
        return this.buildSession().byId(this.entityImplClass);
    }

    protected SessionImpl buildSession() {
        return ((SessionImpl) this.sessionFactory.getCurrentSession());
    }

    @Override
    public EntityMetadata getEntityMetadata() {
        return this.entityMetadata;
    }
}
