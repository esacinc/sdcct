package gov.hhs.onc.sdcct.data.db;

import org.springframework.data.jpa.domain.Specification;

@FunctionalInterface
public interface SdcctCriterion<T> extends Specification<T> {
}
