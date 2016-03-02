package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.SdcctDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSdcctDao<T extends SdcctEntity> extends AbstractSdcctDataAccessor<T, T> implements SdcctDao<T> {
    @Autowired
    protected SessionFactory sessionFactory;

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
            this.buildSession().delete(this.buildRefByNaturalId(naturalId));

            return true;
        }

        return false;
    }

    @Override
    public boolean removeById(Serializable id) throws Exception {
        if (this.existsById(id)) {
            this.buildSession().delete(this.buildRefById(id));

            return true;
        }

        return false;
    }

    @Nonnegative
    @Override
    public long remove(SdcctCriteria criteria) throws Exception {
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

        // noinspection ConstantConditions
        if (bean.hasEntityId() && this.existsById((entityId = bean.getEntityId()))) {
            this.buildSession().update(bean);
        } else {
            bean.setEntityId((entityId = ((Long) this.buildSession().save(bean))));
        }

        return entityId;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> findAll(SdcctCriteria criteria) throws Exception {
        FullTextSession session = this.buildFullTextSession();
        Criteria execCriteria = session.createCriteria(this.entityImplClass);

        List<Long> entityIds = this.findIndexedEntityIds(criteria, session, execCriteria);

        return (!entityIds.isEmpty() ? ((List<T>) execCriteria.add(Restrictions.in(DbPropertyNames.ENTITY_ID, entityIds)).list()) : new ArrayList<>());
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
    public T find(SdcctCriteria criteria) throws Exception {
        FullTextSession session = this.buildFullTextSession();
        Criteria execCriteria = session.createCriteria(this.entityImplClass);

        Long entityId = ((Long) ((Object[]) criteria.setLimit(1).buildQuery(this.entityImplClass, session, execCriteria).uniqueResult())[0]);

        return ((entityId != null) ? this.entityImplClass.cast(execCriteria.add(Restrictions.idEq(entityId)).uniqueResult()) : null);
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
    public boolean exists(SdcctCriteria criteria) throws Exception {
        return (this.count(criteria) > 0);
    }

    @Nonnegative
    @Override
    public long count(SdcctCriteria criteria) throws Exception {
        FullTextSession session = this.buildFullTextSession();
        Criteria execCriteria = session.createCriteria(this.entityImplClass);

        List<Long> entityIds = this.findIndexedEntityIds(criteria, session, execCriteria);

        return (!entityIds.isEmpty() ? ((Long) execCriteria.add(Restrictions.in(DbPropertyNames.ENTITY_ID, entityIds)).setProjection(Projections.rowCount())
            .setMaxResults(1).uniqueResult()) : 0);
    }

    @Override
    public void reindex() throws Exception {
        this.buildFullTextSession().createIndexer(this.entityImplClass).startAndWait();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected List<Long> findIndexedEntityIds(SdcctCriteria criteria, FullTextSession session, Criteria execCriteria) throws Exception {
        return ((List<Object[]>) criteria.buildQuery(this.entityImplClass, session, execCriteria).list()).stream().map(results -> ((Long) results[0]))
            .collect(Collectors.toList());
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

    protected FullTextSession buildFullTextSession() {
        return Search.getFullTextSession(this.buildSession());
    }

    protected Session buildSession() {
        return Search.getFullTextSession(this.sessionFactory.getCurrentSession());
    }
}
