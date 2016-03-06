package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbFieldNames;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.IdentifierEqExpression;
import org.hibernate.criterion.InExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

public class SdcctCriteria<T extends SdcctEntity> extends CriteriaImpl {
    private class IntrospectingQuery implements CriteriaQuery {
        private String propPath;

        @Override
        public SessionFactoryImplementor getFactory() {
            return SdcctCriteria.this.getSession().getFactory();
        }

        @Override
        public String getColumn(Criteria criteria, String propPath) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getColumns(String propPath, Criteria criteria) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] findColumns(String propPath, Criteria criteria) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Type getType(Criteria criteria, String propPath) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getColumnsUsingProjection(Criteria criteria, String propPath) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Type getTypeUsingProjection(Criteria criteria, String propPath) throws HibernateException {
            this.propPath = propPath;

            return SdcctCriteria.this.entityPropMetadatas.get(propPath).getType();
        }

        @Override
        public TypedValue getTypedValue(Criteria criteria, String propPath, Object value) throws HibernateException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getEntityName(Criteria criteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getEntityName(Criteria criteria, String propPath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSQLAlias(Criteria criteria) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getSQLAlias(Criteria criteria, String propPath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getPropertyName(String propName) {
            return propName;
        }

        @Override
        public String[] getIdentifierColumns(Criteria criteria) {
            return ArrayUtils.toArray(DbColumnNames.ENTITY_ID);
        }

        @Override
        public Type getIdentifierType(Criteria criteria) {
            return LongType.INSTANCE;
        }

        @Override
        public TypedValue getTypedIdentifierValue(Criteria criteria, Object value) {
            return new TypedValue(LongType.INSTANCE, value);
        }

        @Override
        public String generateSQLAlias() {
            throw new UnsupportedOperationException();
        }

        public String getPropertyPath() {
            return this.propPath;
        }
    }

    private final static String EQ_OP = "=";

    private final static long serialVersionUID = 0L;

    private Class<T> entityClass;
    private Class<? extends T> entityImplClass;
    private Map<String, PropertyMetadata> entityPropMetadatas;
    private List<Criterion> criterions = new ArrayList<>();
    private Map<String, String> keywords = new LinkedHashMap<>();
    private List<Order> orders = new ArrayList<>();
    private Boolean idOrderDirection;

    public SdcctCriteria(Class<T> entityClass, Class<? extends T> entityImplClass, EntityMetadata entityMetadata) {
        super(entityImplClass.getName(), null);

        this.entityClass = entityClass;
        this.entityImplClass = entityImplClass;
        this.entityPropMetadatas = entityMetadata.getProperties();
    }

    @Nullable
    public T uniqueEntity() throws HibernateException {
        return this.entityClass.cast(this.uniqueResult());
    }

    @Nullable
    @Override
    public Object uniqueResult() throws HibernateException {
        return super.uniqueResult();
    }

    @Nullable
    public T firstEntity() throws HibernateException {
        return this.entityClass.cast(this.firstResult());
    }

    @Nullable
    public Object firstResult() throws HibernateException {
        List<?> results = this.setMaxResults(1).list();

        return (!results.isEmpty() ? results.get(0) : null);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> listEntities() throws HibernateException {
        return ((List<T>) this.list());
    }

    @Override
    public List<?> list() throws HibernateException {
        this.before();

        try {
            if (!this.keywords.isEmpty() && !this.processKeywords()) {
                return Collections.<T>emptyList();
            }

            this.criterions.forEach(super::add);
            this.orders.forEach(super::addOrder);

            return this.getSession().list(this);
        } finally {
            this.after();
        }
    }

    @Override
    public ScrollableResults scroll(ScrollMode scrollMode) {
        throw new UnsupportedOperationException();
    }

    public boolean hasOrder() {
        return this.iterateOrderings().hasNext();
    }

    public SdcctCriteria<T> addOrders(Order ... orders) {
        Stream.of(orders).forEach(this::addOrder);

        return this;
    }

    @Override
    public SdcctCriteria<T> addOrder(Order order) {
        this.orders.add(order);

        if (order.getPropertyName().equals(DbPropertyNames.ENTITY_ID)) {
            this.idOrderDirection = order.isAscending();
        }

        return this;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<T> addKeywords(Entry<String, String> ... keywords) {
        Stream.of(keywords).forEach(keyword -> this.addKeyword(keyword.getKey(), keyword.getValue()));

        return this;
    }

    public SdcctCriteria<T> addKeyword(String keywordPropName, String keywordValue) {
        this.keywords.put(keywordPropName, keywordValue);

        return this;
    }

    public boolean hasCriterions() {
        return !this.criterions.isEmpty();
    }

    public SdcctCriteria<T> addAll(Criterion ... criterions) {
        Stream.of(criterions).forEach(this::add);

        return this;
    }

    @Override
    public SdcctCriteria<T> add(Criterion criterion) {
        this.criterions.add(criterion);

        return this;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private boolean processKeywords() {
        Long eqCriterionId = null;
        Set<Long> inCriterionIds = new LinkedHashSet<>();

        if (this.hasCriterions()) {
            Iterator<Criterion> criterionIterator = this.criterions.iterator();
            Criterion criterion;
            IntrospectingQuery introspectingQuery = new IntrospectingQuery();
            SimpleExpression criterionExpr;
            Object criterionValue;
            TypedValue[] typedCriterionValues;

            while (criterionIterator.hasNext()) {
                if (((criterion = criterionIterator.next()) instanceof IdentifierEqExpression)) {
                    inCriterionIds.add((eqCriterionId = ((Long) criterion.getTypedValues(this, introspectingQuery)[0].getValue())));

                    criterionIterator.remove();
                } else if ((criterion instanceof SimpleExpression)
                    && ((criterionExpr = ((SimpleExpression) criterion)).getPropertyName().equals(DbPropertyNames.ENTITY_ID))
                    && StringUtils.removeEnd(StringUtils.removeStart(criterionExpr.toString(), DbPropertyNames.ENTITY_ID),
                        Objects.toString((criterionValue = criterionExpr.getValue()))).equals(EQ_OP)) {
                    inCriterionIds.add((eqCriterionId = ((Long) criterionValue)));

                    criterionIterator.remove();
                } else if (criterion instanceof InExpression) {
                    typedCriterionValues = criterion.getTypedValues(this, introspectingQuery);

                    if (introspectingQuery.getPropertyPath().equals(DbPropertyNames.ENTITY_ID)) {
                        Stream.of(typedCriterionValues).forEach(typedCriterionValue -> inCriterionIds.add(((Long) typedCriterionValue.getValue())));

                        criterionIterator.remove();
                    }
                }
            }
        }

        FullTextSession fullTextSession = Search.getFullTextSession(this.getSession());
        QueryBuilder queryBuilder = fullTextSession.getSearchFactory().buildQueryBuilder().forEntity(this.entityImplClass).get();
        BooleanJunction<?> conjunction = queryBuilder.bool();

        if (eqCriterionId != null) {
            conjunction = conjunction.must(queryBuilder.keyword().onField(DbFieldNames.ENTITY_ID).matching(eqCriterionId).createQuery());
        }

        PropertyMetadata keywordPropMetadata;

        for (String keywordPropName : this.keywords.keySet()) {
            conjunction =
                conjunction.must(queryBuilder
                    .keyword()
                    .onFields((keywordPropMetadata = this.entityPropMetadatas.get(keywordPropName)).getLowercaseFieldName(),
                        keywordPropMetadata.getEdgeNgramFieldName(), keywordPropMetadata.getNgramFieldName(), keywordPropMetadata.getPhoneticFieldName())
                    .matching(this.keywords.get(keywordPropName)).createQuery());
        }

        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(conjunction.createQuery(), this.entityImplClass);
        fullTextQuery.setProjection(ProjectionConstants.ID);

        boolean inCriterionIdsAvailable = !inCriterionIds.isEmpty();
        Integer numInCriterionIds = inCriterionIds.size();

        if (this.hasMaxResults()) {
            // noinspection ConstantConditions
            int maxResults = this.getMaxResults();

            fullTextQuery.setMaxResults((inCriterionIdsAvailable ? maxResults : Math.min(maxResults, numInCriterionIds)));
        } else if (inCriterionIdsAvailable) {
            fullTextQuery.setMaxResults(numInCriterionIds);
        }

        if (this.idOrderDirection != null) {
            fullTextQuery.setSort(new Sort(new SortField(DbFieldNames.ENTITY_ID, SortField.Type.LONG, this.idOrderDirection)));
        }

        Stream<Long> fullTextIdStream = ((List<Object[]>) fullTextQuery.list()).stream().map(fullTextResult -> ((Long) fullTextResult[0]));

        if (inCriterionIdsAvailable) {
            fullTextIdStream = fullTextIdStream.filter(inCriterionIds::contains);
        }

        List<Long> fullTextIds = fullTextIdStream.collect(Collectors.toList());

        if (fullTextIds.isEmpty()) {
            return false;
        }

        int numFullTextIds = fullTextIds.size();

        if (numFullTextIds == 1) {
            this.add(Restrictions.idEq(fullTextIds.get(0)));
        } else {
            this.add(Restrictions.in(DbPropertyNames.ENTITY_ID, fullTextIds));
        }

        this.setMaxResults(numFullTextIds);

        return true;
    }

    public boolean hasProjection() {
        return (this.getProjection() != null);
    }

    @Nullable
    @Override
    public Projection getProjection() {
        return super.getProjection();
    }

    @Override
    public SdcctCriteria<T> setProjection(Projection projection) {
        super.setProjection(projection);

        return this;
    }

    public boolean hasMaxResults() {
        return (this.getMaxResults() != null);
    }

    @Nonnegative
    @Nullable
    @Override
    public Integer getMaxResults() {
        return super.getMaxResults();
    }

    @Override
    public SdcctCriteria<T> setMaxResults(@Nonnegative int maxResults) {
        super.setMaxResults(maxResults);

        return this;
    }

    @Override
    public SessionImpl getSession() {
        return ((SessionImpl) super.getSession());
    }

    public SdcctCriteria<T> setSession(SessionImpl session) {
        super.setSession(session);

        return this;
    }
}
