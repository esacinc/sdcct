package gov.hhs.onc.sdcct.data.db.criteria.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriterion;
import javax.annotation.Nonnegative;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public final class SdcctCriterionUtils {
    private SdcctCriterionUtils() {
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchParam(String paramsPropName, String paramName, Object paramValue) {
        return (builder, query, root) -> {
            Join<?, ?> paramJoin = root.join(paramsPropName);

            return builder.and(builder.equal(paramJoin.get(DbPropertyNames.NAME), paramName), builder.equal(paramJoin.get(DbPropertyNames.VALUE), paramValue));
        };
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchInstances() {
        return (builder, query, root) -> builder.notEqual(root.get(DbPropertyNames.INSTANCE_ID), -1L);
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchDeleted() {
        return (builder, query, root) -> builder.isNotNull(root.get(DbPropertyNames.DELETED_TIMESTAMP));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static <T extends SdcctResource> SdcctCriterion<T> matchLatestVersion() {
        return (builder, query, root) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<T> subqueryRoot = ((Root<T>) subquery.from(root.getJavaType()));

            subquery.select(builder.max(subqueryRoot.get(DbPropertyNames.VERSION)));
            subquery.where(builder.equal(subqueryRoot.get(DbPropertyNames.ID), root.get(DbPropertyNames.ID)));

            return builder.equal(root.get(DbPropertyNames.VERSION), subquery);
        };
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchVersion(@Nonnegative long version) {
        return (builder, query, root) -> builder.equal(root.get(DbPropertyNames.VERSION), version);
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchInstanceId(long instanceId) {
        return (builder, query, root) -> builder.equal(root.get(DbPropertyNames.INSTANCE_ID), instanceId);
    }

    public static <T extends SdcctResource> SdcctCriterion<T> matchId(@Nonnegative long id) {
        return (builder, query, root) -> builder.equal(root.get(DbPropertyNames.ID), id);
    }

    public static <T extends SdcctEntity> SdcctCriterion<T> matchEntityId(long entityId) {
        return (builder, query, root) -> builder.equal(root.get(DbPropertyNames.ENTITY_ID), entityId);
    }
}
