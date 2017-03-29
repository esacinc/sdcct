package gov.hhs.onc.sdcct.testcases.results.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResult;
import gov.hhs.onc.sdcct.testcases.results.SdcctTestcaseResultHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnegative;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Results;
import net.sf.ehcache.search.attribute.AttributeExtractor;
import net.sf.ehcache.search.expression.BaseCriteria;
import org.springframework.cache.ehcache.EhCacheCache;

public abstract class AbstractSdcctTestcaseResultHandler<T extends SdcctTestcaseResult<?, ?, ?>> implements SdcctTestcaseResultHandler<T> {
    private class SubmittedAfterTimestampCriteria extends BaseCriteria {
        private long submittedTimestamp;

        public SubmittedAfterTimestampCriteria(@Nonnegative long submittedTimestamp) {
            this.submittedTimestamp = submittedTimestamp;
        }

        @Override
        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        public boolean execute(Element elem, Map<String, AttributeExtractor> attrExtractors) {
            return (((T) elem.getObjectValue()).getSubmission().getSubmittedTimestamp() >= this.submittedTimestamp);
        }

        @Override
        public Set<Attribute<?>> getAttributes() {
            return Collections.emptySet();
        }
    }

    private final static Comparator<SdcctTestcaseResult<?, ?, ?>> PROCESSED_TIMESTAMP_RESULT_COMPARATOR =
        Comparator.comparing(SdcctTestcaseResult::getProcessedTimestamp);

    private EhCacheCache resultCache;
    private Ehcache resultEhcache;

    @Override
    public void addResult(T result) {
        this.resultEhcache.put(new Element(result.getTxId(), result));
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public List<T> findResults(@Nonnegative long submittedAfterTimestamp) {
        Results cachedResults =
            this.resultEhcache.createQuery().addCriteria(new SubmittedAfterTimestampCriteria(submittedAfterTimestamp)).includeValues().execute();
        int numCachedResults = cachedResults.size();

        return ((numCachedResults > 0)
            ? cachedResults.all().stream().map(cachedResult -> ((T) cachedResult.getValue())).sorted(PROCESSED_TIMESTAMP_RESULT_COMPARATOR)
                .collect(Collectors.toCollection(() -> new ArrayList<>(numCachedResults)))
            : Collections.emptyList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.resultEhcache = this.resultCache.getNativeCache();
    }

    public EhCacheCache getResultCache() {
        return this.resultCache;
    }

    public void setResultCache(EhCacheCache resultCache) {
        this.resultCache = resultCache;
    }
}
