package gov.hhs.onc.sdcct.data.db.criteria;

import javax.persistence.criteria.CommonAbstractCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@FunctionalInterface
public interface SdcctCriterion<T> {
    public default SdcctCriterion<T> not() {
        return (builder, query, root) -> this.toPredicate(builder, query, root).not();
    }

    public Predicate toPredicate(CriteriaBuilder builder, CommonAbstractCriteria query, Root<T> root);
}
