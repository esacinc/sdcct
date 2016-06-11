package gov.hhs.onc.sdcct.data.db.criteria;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctEntityAccessor;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;

public interface SdcctCriteria<T extends SdcctEntity> extends SdcctEntityAccessor<T> {
    @Nonnegative
    public long delete(EntityManager entityManager);

    @Nonnegative
    public <U> long delete(EntityManager entityManager, CriteriaDelete<U> criteriaQuery, Root<T> root);

    @Nonnegative
    public long update(EntityManager entityManager);

    @Nonnegative
    public <U> long update(EntityManager entityManager, CriteriaUpdate<U> criteriaQuery, Root<T> root);

    public boolean exists(EntityManager entityManager);

    @Nonnegative
    public long count(EntityManager entityManager);

    @Nullable
    public T first(EntityManager entityManager);

    @Nullable
    public <U> U first(EntityManager entityManager, CriteriaQuery<U> criteriaQuery, Root<T> root);

    public List<T> list(EntityManager entityManager);

    public <U> List<U> list(EntityManager entityManager, CriteriaQuery<U> criteriaQuery, Root<T> root);

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<T> addCriterions(SdcctCriterion<T> ... criterions);

    public boolean hasCriterions();

    public List<SdcctCriterion<T>> getCriterions();

    public SdcctCriteria<T> addKeyword(String keywordPropName, String keywordValue);

    public boolean hasKeywords();

    public MultiValueMap<String, String> getKeywords();

    public boolean hasMaxResults();

    @Nonnegative
    @Nullable
    public Integer getMaxResults();

    public SdcctCriteria<T> setMaxResults(@Nonnegative @Nullable Integer maxResults);

    public boolean hasSort();

    @Nullable
    public Sort getSort();

    public SdcctCriteria<T> setSort(@Nullable Sort sort);
}
