package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.engine.ProjectionConstants;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.QueryContextBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class SdcctCriteria {
    private List<SdcctCriterion> criterions = new ArrayList<>();
    private Integer limit;
    private List<SdcctOrder> orders = new ArrayList<>();

    public FullTextQuery buildQuery(Class<? extends SdcctEntity> entityClass, FullTextSession fullTextSession, Criteria execCriteria) throws Exception {
        MultiValueMap<Class<? extends SdcctEntity>, SdcctCriterion> entityCriterions = new LinkedMultiValueMap<>(this.criterions.size());

        this.criterions.forEach(criterion -> entityCriterions.add((criterion.hasEntityClass() ? criterion.getEntityClass() : entityClass), criterion));

        MultiValueMap<Class<? extends SdcctEntity>, SdcctOrder> entityOrders = new LinkedMultiValueMap<>(this.orders.size());

        this.orders.forEach(order -> entityOrders.add((order.hasEntityClass() ? order.getEntityClass() : entityClass), order));

        SearchFactory searchFactory = fullTextSession.getSearchFactory();
        Map<Class<? extends SdcctEntity>, IndexedTypeDescriptor> indexedEntityTypeDescs =
            entityCriterions.keySet().stream().collect(Collectors.toMap(Function.identity(), searchFactory::getIndexedTypeDescriptor));
        Map<Class<? extends SdcctEntity>, Map<String, String>> indexedEntityProps =
            indexedEntityTypeDescs
                .entrySet()
                .stream()
                .filter(indexedEntityTypeDescEntry -> indexedEntityTypeDescEntry.getValue().isIndexed())
                .collect(
                    Collectors.toMap(
                        Entry::getKey,
                        indexedEntityTypeDescEntry -> indexedEntityTypeDescEntry
                            .getValue()
                            .getIndexedProperties()
                            .stream()
                            .collect(
                                Collectors.toMap(PropertyDescriptor::getName, indexedEntityPropDesc -> indexedEntityPropDesc.getIndexedFields().iterator()
                                    .next().getName()))));
        QueryContextBuilder queryContextBuilder = searchFactory.buildQueryBuilder();
        Map<Class<? extends SdcctEntity>, QueryBuilder> entityQueryBuilders =
            entityCriterions.keySet().stream()
                .collect(Collectors.toMap(Function.identity(), queryEntityClass -> queryContextBuilder.forEntity(queryEntityClass).get()));
        List<Query> queries = new ArrayList<>(), entityQueries;
        Query entityQuery, query;
        BooleanJunction<?> entityBooleanJunction;
        boolean entityPropIndexed;
        Map<String, String> indexedEntityPropFieldNames;
        QueryBuilder entityQueryBuilder;
        List<SdcctCriterion> entityCriterionItems;
        String entityPropName, entityFieldName;
        Object entityCriterionValue;
        Object[] entityCriterionValues;
        List<SdcctOrder> entityOrderItems;

        for (Class<? extends SdcctEntity> criterionEntityClass : entityCriterions.keySet()) {
            indexedEntityPropFieldNames = (indexedEntityTypeDescs.get(criterionEntityClass).isIndexed() ? indexedEntityProps.get(criterionEntityClass) : null);
            entityQueryBuilder = entityQueryBuilders.get(criterionEntityClass);
            entityQueries = new ArrayList<>();

            if (entityCriterions.containsKey(criterionEntityClass) && !(entityCriterionItems = entityCriterions.get(criterionEntityClass)).isEmpty()) {
                for (SdcctCriterion entityCriterion : entityCriterionItems) {
                    // noinspection ConstantConditions
                    entityFieldName =
                        ((entityPropIndexed = indexedEntityPropFieldNames.containsKey((entityPropName = entityCriterion.getPropertyName())))
                            ? indexedEntityPropFieldNames.get(entityPropName) : null);
                    entityCriterionValue = entityCriterion.getValue();

                    switch (entityCriterion.getOperation()) {
                        case EQ:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.keyword().onField(entityFieldName).matching(entityCriterionValue).createQuery());
                            } else {
                                execCriteria.add(Restrictions.eq(entityPropName, entityCriterionValue));
                            }
                            break;

                        case NE:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.bool()
                                    .must(entityQueryBuilder.keyword().onField(entityFieldName).matching(entityCriterionValue).createQuery()).not()
                                    .createQuery());
                            } else {
                                execCriteria.add(Restrictions.ne(entityPropName, entityCriterionValue));
                            }
                            break;

                        case GT:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.range().onField(entityFieldName).above(entityCriterionValue).createQuery());
                            } else {
                                execCriteria.add(Restrictions.gt(entityPropName, entityCriterionValue));
                            }
                            break;

                        case LT:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.range().onField(entityFieldName).below(entityCriterionValue).createQuery());
                            } else {
                                execCriteria.add(Restrictions.lt(entityPropName, entityCriterionValue));
                            }
                            break;

                        case GE:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.range().onField(entityFieldName).from(entityCriterionValue).to(null).excludeLimit()
                                    .createQuery());
                            } else {
                                execCriteria.add(Restrictions.ge(entityPropName, entityCriterionValue));
                            }
                            break;

                        case LE:
                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.range().onField(entityFieldName).from(null).excludeLimit().to(entityCriterionValue)
                                    .createQuery());
                            } else {
                                execCriteria.add(Restrictions.le(entityPropName, entityCriterionValue));
                            }
                            break;

                        case BETWEEN:
                            entityCriterionValues = ((Object[]) entityCriterionValue);

                            if (entityPropIndexed) {
                                entityQueries.add(entityQueryBuilder.range().onField(entityFieldName).from(entityCriterionValues[0])
                                    .to(entityCriterionValues[1]).createQuery());
                            } else {
                                execCriteria.add(Restrictions.between(entityPropName, entityCriterionValues[0], entityCriterionValues[1]));
                            }
                            break;
                    }
                }
            }

            if (!entityQueries.isEmpty()) {
                entityBooleanJunction = entityQueryBuilder.bool();

                for (Query entityQueryItem : entityQueries) {
                    entityBooleanJunction = entityBooleanJunction.must(entityQueryItem);
                }

                entityQuery = entityBooleanJunction.createQuery();
            } else {
                entityQuery = entityQueryBuilder.all().createQuery();
            }

            queries.add(entityQuery);
        }

        entityQueryBuilder = entityQueryBuilders.get(entityClass);

        if (!queries.isEmpty()) {
            entityBooleanJunction = entityQueryBuilder.bool();

            for (Query queryItem : queries) {
                entityBooleanJunction = entityBooleanJunction.must(queryItem);
            }

            query = entityBooleanJunction.createQuery();
        } else {
            query = entityQueryBuilder.all().createQuery();
        }

        FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(query, entityClass);
        fullTextQuery.setProjection(ProjectionConstants.ID);

        if (this.hasLimit()) {
            fullTextQuery.setMaxResults(this.limit);
            execCriteria.setMaxResults(this.limit);
        }
        
        // TODO: order/sorting

        return fullTextQuery;
    }

    public boolean hasCriterions() {
        return !this.criterions.isEmpty();
    }

    public List<SdcctCriterion> getCriterions() {
        return this.criterions;
    }

    public SdcctCriteria setCriterions(SdcctCriterion ... criterions) {
        this.criterions.clear();

        return this.addCriterions(criterions);
    }

    public SdcctCriteria addCriterions(SdcctCriterion ... criterions) {
        Stream.of(criterions).forEach(this.criterions::add);

        return this;
    }

    public boolean hasLimit() {
        return (this.limit != null);
    }

    @Nonnegative
    @Nullable
    public Integer getLimit() {
        return this.limit;
    }

    public SdcctCriteria setLimit(@Nonnegative @Nullable Integer limit) {
        this.limit = limit;

        return this;
    }

    public boolean hasOrders() {
        return !this.orders.isEmpty();
    }

    public List<SdcctOrder> getOrders() {
        return this.orders;
    }

    public SdcctCriteria setOrders(SdcctOrder ... orders) {
        this.orders.clear();

        return this.addOrders(orders);
    }

    public SdcctCriteria addOrders(SdcctOrder ... orders) {
        Stream.of(orders).forEach(this.orders::add);

        return this;
    }
}
