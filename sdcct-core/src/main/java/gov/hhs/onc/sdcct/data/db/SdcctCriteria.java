package gov.hhs.onc.sdcct.data.db;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import java.util.List;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;

public interface SdcctCriteria<T extends SdcctEntity> extends SdcctEntityAccessor<T> {
    public boolean exists(EntityManager entityManager);

    @Nonnegative
    public long count(EntityManager entityManager);

    @Nullable
    public T first(EntityManager entityManager);

    public List<T> list(EntityManager entityManager);

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
