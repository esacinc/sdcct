package gov.hhs.onc.sdcct.data.db.criteria.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriterion;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctEntityAccessor;
import gov.hhs.onc.sdcct.data.db.logging.impl.LoggingIndexQueryInterceptor;
import gov.hhs.onc.sdcct.data.db.metadata.EntityMetadata;
import gov.hhs.onc.sdcct.data.db.metadata.PropertyMetadata;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Weight;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SdcctCriteriaImpl<T extends SdcctEntity> extends AbstractSdcctEntityAccessor<T> implements SdcctCriteria<T> {
    @SuppressWarnings({ CompilerWarnings.DEPRECATION })
    private class InterceptingQuery extends org.apache.lucene.search.Query {
        private org.apache.lucene.search.Query delegate;

        public InterceptingQuery(org.apache.lucene.search.Query delegate) {
            this.delegate = delegate;
        }

        @Override
        public org.apache.lucene.search.Query rewrite(IndexReader reader) throws IOException {
            return SdcctCriteriaImpl.this.indexQueryInterceptor.intercept(delegate.rewrite(reader));
        }

        @Override
        public Weight createWeight(IndexSearcher searcher, boolean needsScores) throws IOException {
            return delegate.createWeight(searcher, needsScores);
        }

        @Override
        public String toString(String field) {
            return delegate.toString(field);
        }

        @Override
        @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }
    }

    private LoggingIndexQueryInterceptor indexQueryInterceptor;
    private CriteriaBuilder criteriaBuilder;
    private Map<String, PropertyMetadata> entityPropMetadatas;
    private List<SdcctCriterion<T>> criterions = new ArrayList<>();
    private MultiValueMap<String, String> keywords = new LinkedMultiValueMap<>();
    private Integer maxResults;
    private Sort sort;

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteriaImpl(CriteriaBuilder criteriaBuilder, Class<T> entityClass, Class<? extends T> entityImplClass, EntityMetadata entityMetadata,
        LoggingIndexQueryInterceptor indexQueryInterceptor, SdcctCriterion<T> ... criterions) {
        super(entityClass, entityImplClass);

        this.criteriaBuilder = criteriaBuilder;
        this.indexQueryInterceptor = indexQueryInterceptor;
        this.entityPropMetadatas = entityMetadata.getProperties();

        this.addCriterions(criterions);
    }

    @Nonnegative
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public long delete(EntityManager entityManager) {
        CriteriaDelete<T> criteriaQuery = ((CriteriaDelete<T>) this.criteriaBuilder.createCriteriaDelete(this.entityImplClass));

        return this.delete(entityManager, criteriaQuery, criteriaQuery.from(((Class<T>) this.entityImplClass)));
    }

    @Nonnegative
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <U> long delete(EntityManager entityManager, CriteriaDelete<U> criteriaQuery, Root<T> root) {
        Query query = this.buildQuery(entityManager, criteriaQuery, root, this.maxResults);

        if (query == null) {
            return 0L;
        }

        return query.executeUpdate();
    }

    @Nonnegative
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public long update(EntityManager entityManager) {
        CriteriaUpdate<T> criteriaQuery = ((CriteriaUpdate<T>) this.criteriaBuilder.createCriteriaUpdate(this.entityImplClass));

        return this.update(entityManager, criteriaQuery, criteriaQuery.from(((Class<T>) this.entityImplClass)));
    }

    @Nonnegative
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <U> long update(EntityManager entityManager, CriteriaUpdate<U> criteriaQuery, Root<T> root) {
        Query query = this.buildQuery(entityManager, criteriaQuery, root, this.maxResults);

        if (query == null) {
            return 0L;
        }

        return query.executeUpdate();
    }

    @Override
    public boolean exists(EntityManager entityManager) {
        return (this.count(entityManager, 1) > 0);
    }

    @Nonnegative
    @Override
    public long count(EntityManager entityManager) {
        return this.count(entityManager, this.maxResults);
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public T first(EntityManager entityManager) {
        CriteriaQuery<T> criteriaQuery = ((CriteriaQuery<T>) this.criteriaBuilder.createQuery(this.entityImplClass));

        return this.first(entityManager, criteriaQuery, ((Root<T>) criteriaQuery.from(this.entityImplClass)));
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <U> U first(EntityManager entityManager, CriteriaQuery<U> criteriaQuery, Root<T> root) {
        TypedQuery<U> query = this.buildQuery(entityManager, criteriaQuery, root, 1);

        if (query == null) {
            return null;
        }

        List<U> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }

        U result = results.get(0);

        if (result instanceof SdcctEntity) {
            entityManager.detach(result);
        }

        return result;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> list(EntityManager entityManager) {
        CriteriaQuery<T> criteriaQuery = ((CriteriaQuery<T>) this.criteriaBuilder.createQuery(this.entityImplClass));

        return this.list(entityManager, criteriaQuery, ((Root<T>) criteriaQuery.from(this.entityImplClass)));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <U> List<U> list(EntityManager entityManager, CriteriaQuery<U> criteriaQuery, Root<T> root) {
        return Optional.ofNullable(this.buildQuery(entityManager, criteriaQuery, root, this.maxResults))
            .map(query -> query.getResultList().stream().map(result -> {
                if (result instanceof SdcctEntity) {
                    entityManager.detach(result);
                }

                return result;
            }).collect(Collectors.toList())).orElseGet(Collections::emptyList);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private long count(EntityManager entityManager, @Nullable Integer maxResults) {
        CriteriaQuery<Long> criteriaQuery = this.criteriaBuilder.createQuery(Long.class);
        Root<T> root = ((Root<T>) criteriaQuery.from(this.entityImplClass));

        return Optional.ofNullable(this.buildQuery(entityManager, criteriaQuery.select(this.criteriaBuilder.count(root)), root, maxResults))
            .map(TypedQuery::getSingleResult).orElse(0L);
    }

    @Nullable
    private <U> TypedQuery<U> buildQuery(EntityManager entityManager, CriteriaQuery<U> criteriaQuery, Root<T> root, @Nullable Integer maxResults) {
        if (this.hasSort()) {
            criteriaQuery.orderBy(QueryUtils.toOrders(this.sort, root, this.criteriaBuilder));
        }

        return this.buildQuery(entityManager, EntityManager::createQuery, criteriaQuery, CriteriaQuery::where, root, maxResults);
    }

    @Nullable
    private <U> Query buildQuery(EntityManager entityManager, CriteriaDelete<U> criteriaQuery, Root<T> root, @Nullable Integer maxResults) {
        return this.buildQuery(entityManager, EntityManager::createQuery, criteriaQuery, CriteriaDelete::where, root, maxResults);
    }

    @Nullable
    private <U> Query buildQuery(EntityManager entityManager, CriteriaUpdate<U> criteriaQuery, Root<T> root, @Nullable Integer maxResults) {
        return this.buildQuery(entityManager, EntityManager::createQuery, criteriaQuery, CriteriaUpdate::where, root, maxResults);
    }

    @Nullable
    private <U, V extends CommonAbstractCriteria, W extends Query> W buildQuery(EntityManager entityManager, BiFunction<EntityManager, V, W> queryBuilder,
        V criteriaQuery, BiFunction<V, Predicate[], V> whereBuilder, Root<T> root, @Nullable Integer maxResults) {
        Predicate predicate = null;

        if (this.hasKeywords()) {
            Long[] fullTextEntityIds = this.processKeywords(entityManager, maxResults);

            if (ArrayUtils.isEmpty(fullTextEntityIds)) {
                return null;
            }

            Path<BigInteger> fullTextEntityIdPath = root.get(DbPropertyNames.ENTITY_ID);

            predicate = ((fullTextEntityIds.length == 1)
                ? this.criteriaBuilder.equal(fullTextEntityIdPath, fullTextEntityIds[0])
                : this.criteriaBuilder.in(fullTextEntityIdPath).in(((Object[]) fullTextEntityIds)));

            maxResults = ((maxResults != null) ? Math.min(maxResults, fullTextEntityIds.length) : fullTextEntityIds.length);
        }

        if (this.hasCriterions()) {
            Predicate criterionsPredicate = this.criteriaBuilder
                .and(this.criterions.stream().map(criterion -> criterion.toPredicate(this.criteriaBuilder, criteriaQuery, root)).toArray(Predicate[]::new));

            predicate = ((predicate != null) ? this.criteriaBuilder.and(predicate, criterionsPredicate) : criterionsPredicate);
        }

        if (predicate != null) {
            whereBuilder.apply(criteriaQuery, ArrayUtils.toArray(predicate));
        }

        W query = queryBuilder.apply(entityManager, criteriaQuery);

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }

        return query;
    }

    @Nullable
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private Long[] processKeywords(EntityManager entityManager, @Nullable Integer maxResults) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(this.entityImplClass).get();
        BooleanJunction<?> conjunction = queryBuilder.bool();

        PropertyMetadata keywordPropMetadata;

        for (String keywordPropName : this.keywords.keySet()) {
            conjunction = conjunction.must(queryBuilder.keyword()
                .onFields((keywordPropMetadata = this.entityPropMetadatas.get(keywordPropName)).getLowercaseFieldName(),
                    keywordPropMetadata.getEdgeNgramFieldName(), keywordPropMetadata.getNgramFieldName(), keywordPropMetadata.getPhoneticFieldName())
                .matching(StringUtils.join(this.keywords.get(keywordPropName), StringUtils.SPACE)).createQuery());
        }

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(new InterceptingQuery(conjunction.createQuery()), this.entityImplClass);
        fullTextQuery.setProjection(ProjectionConstants.ID);

        if (maxResults != null) {
            fullTextQuery.setMaxResults(maxResults);
        }

        return ((List<Object[]>) fullTextQuery.getResultList()).stream().map(fullTextResult -> ((Long) fullTextResult[0])).toArray(Long[]::new);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<T> addCriterions(SdcctCriterion<T> ... criterions) {
        Stream.of(criterions).forEach(this.criterions::add);

        return this;
    }

    @Override
    public boolean hasCriterions() {
        return !this.criterions.isEmpty();
    }

    @Override
    public List<SdcctCriterion<T>> getCriterions() {
        return this.criterions;
    }

    @Override
    public SdcctCriteria<T> addKeyword(String keywordPropName, String keywordValue) {
        this.keywords.add(keywordPropName, keywordValue);

        return this;
    }

    @Override
    public boolean hasKeywords() {
        return !this.keywords.isEmpty();
    }

    @Override
    public MultiValueMap<String, String> getKeywords() {
        return this.keywords;
    }

    @Override
    public boolean hasMaxResults() {
        return (this.maxResults != null);
    }

    @Nullable
    @Override
    public Integer getMaxResults() {
        return this.maxResults;
    }

    @Override
    public SdcctCriteria<T> setMaxResults(@Nonnegative @Nullable Integer maxResults) {
        this.maxResults = maxResults;

        return this;
    }

    @Override
    public boolean hasSort() {
        return (this.sort != null);
    }

    @Nullable
    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public SdcctCriteria<T> setSort(@Nullable Sort sort) {
        this.sort = sort;

        return this;
    }
}
