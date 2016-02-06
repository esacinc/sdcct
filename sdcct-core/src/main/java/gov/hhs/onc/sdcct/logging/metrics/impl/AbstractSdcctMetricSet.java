package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import gov.hhs.onc.sdcct.logging.metrics.SdcctMetricSet;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSdcctMetricSet implements SdcctMetricSet {
    protected static class SdcctGauge<T> implements Gauge<T> {
        protected T value;

        public SdcctGauge(@Nullable T value) {
            this.value = value;
        }

        @Nullable
        @Override
        public T getValue() {
            return this.value;
        }
    }

    protected static class SdcctRatioGauge extends SdcctGauge<Double> {
        public SdcctRatioGauge(@Nullable Number numerator, @Nullable Number denominator) {
            this(numerator, denominator, -1.0D);
        }

        public SdcctRatioGauge(@Nullable Number numerator, @Nullable Number denominator, double defaultIfInvalid) {
            super(defaultIfInvalid);

            if ((numerator != null) && (denominator != null)) {
                double doubleNumerator = numerator.doubleValue(), doubleDenominator = denominator.doubleValue();

                if (Double.isFinite(doubleNumerator) && (doubleNumerator >= 0) && Double.isFinite(doubleDenominator) && (doubleDenominator > 0)) {
                    this.value = (doubleNumerator / doubleDenominator);
                }
            }
        }
    }

    protected final static String AFTER_METRIC_NAME_PART = "after";
    protected final static String BEFORE_METRIC_NAME_PART = "before";
    protected final static String CAPACITY_METRIC_NAME_PART = "capacity";
    protected final static String COLLECTION_METRIC_NAME_PART = "collection";
    protected final static String COMMITTED_METRIC_NAME_PART = "committed";
    protected final static String COUNT_METRIC_NAME_PART = "count";
    protected final static String DURATION_METRIC_NAME_PART = "duration";
    protected final static String END_METRIC_NAME_PART = "end";
    protected final static String EXCEEDED_METRIC_NAME_PART = "exceeded";
    protected final static String FREE_METRIC_NAME_PART = "free";
    protected final static String HEAP_METRIC_NAME_PART = "heap";
    protected final static String HOST_METRIC_NAME_PART = "host";
    protected final static String ID_METRIC_NAME_PART = "id";
    protected final static String INITIAL_METRIC_NAME_PART = "initial";
    protected final static String LAST_METRIC_NAME_PART = "last";
    protected final static String LOAD_METRIC_NAME_PART = "load";
    protected final static String MAX_METRIC_NAME_PART = "max";
    protected final static String MEMORY_METRIC_NAME_PART = "memory";
    protected final static String NAME_METRIC_NAME_PART = "name";
    protected final static String PEAK_METRIC_NAME_PART = "peak";
    protected final static String POOL_METRIC_NAME_PART = "pool";
    protected final static String SPEC_METRIC_NAME_PART = "spec";
    protected final static String START_METRIC_NAME_PART = "start";
    protected final static String STARTED_METRIC_NAME_PART = START_METRIC_NAME_PART + "ed";
    protected final static String STATE_METRIC_NAME_PART = "state";
    protected final static String TIME_METRIC_NAME_PART = "time";
    protected final static String THREAD_METRIC_NAME_PART = "thread";
    protected final static String THRESHOLD_METRIC_NAME_PART = "threshold";
    protected final static String TOTAL_METRIC_NAME_PART = "total";
    protected final static String TYPE_METRIC_NAME_PART = "type";
    protected final static String UP_METRIC_NAME_PART = "up";
    protected final static String USAGE_METRIC_NAME_PART = "usage";
    protected final static String USED_METRIC_NAME_PART = "used";
    protected final static String VENDOR_METRIC_NAME_PART = "vendor";
    protected final static String VERSION_METRIC_NAME_PART = "version";
    protected final static String VM_METRIC_NAME_PART = "vm";

    protected final static String TIME_END_METRIC_NAME_PREFIX = buildMetricName(TIME_METRIC_NAME_PART, END_METRIC_NAME_PART);
    protected final static String TIME_START_METRIC_NAME_PREFIX = buildMetricName(TIME_METRIC_NAME_PART, START_METRIC_NAME_PART);
    protected final static String TIME_UP_METRIC_NAME_PREFIX = buildMetricName(TIME_METRIC_NAME_PART, UP_METRIC_NAME_PART);

    protected final static Pattern METRIC_DISPLAY_NAME_NORMALIZE_REPLACE_PATTERN = Pattern.compile("\\s+");

    @Override
    public Map<String, Metric> getMetrics() {
        return new HashMap<>();
    }

    protected static void registerMemoryUsageMetrics(Map<String, Metric> metrics, String metricNamePrefix, @Nullable MemoryUsage memUsage) {
        if (memUsage != null) {
            registerMemoryUsageMetrics(metrics, metricNamePrefix, memUsage.getCommitted(), memUsage.getInit(), -1, memUsage.getUsed(), memUsage.getMax());
        }
    }

    protected static void registerMemoryUsageMetrics(Map<String, Metric> metrics, String metricNamePrefix, long memCommitted, long memInitial, long memFree,
        long memUsed, long memMax) {
        registerGauge(metrics, buildMetricName(metricNamePrefix, COMMITTED_METRIC_NAME_PART), memCommitted);
        registerGauge(metrics, buildMetricName(metricNamePrefix, INITIAL_METRIC_NAME_PART), memInitial);

        registerMemoryUsageMetrics(metrics, metricNamePrefix, memFree, memUsed, memMax);
    }

    protected static void registerMemoryUsageMetrics(Map<String, Metric> metrics, String metricNamePrefix, long memFree, long memUsed, long memMax) {
        boolean memFreeAvailable = (memFree >= 0), memUsedAvailable = (memUsed >= 0);

        if (memMax > 0) {
            if (!memFreeAvailable && memUsedAvailable) {
                memFree = (memMax - memUsed);
            } else if (memFreeAvailable && !memUsedAvailable) {
                memUsed = (memMax - memFree);
            }
        }

        registerGauge(metrics, buildMetricName(metricNamePrefix, FREE_METRIC_NAME_PART), memFree);
        registerGauge(metrics, buildMetricName(metricNamePrefix, USED_METRIC_NAME_PART), memUsed);
        registerGauge(metrics, buildMetricName(metricNamePrefix, MAX_METRIC_NAME_PART), memMax);
        registerRatioGauge(metrics, buildMetricName(metricNamePrefix, USAGE_METRIC_NAME_PART), memUsed, memMax);
    }

    protected static void registerRatioGauge(Map<String, Metric> metrics, String gaugeName, @Nullable Number gaugeValueNumerator,
        @Nullable Number gaugeValueDenominator) {
        metrics.put(gaugeName, new SdcctRatioGauge(gaugeValueNumerator, gaugeValueDenominator));
    }

    protected static <T> void registerGauge(Map<String, Metric> metrics, String gaugeName, @Nullable T gaugeValue) {
        metrics.put(gaugeName, new SdcctGauge<>(gaugeValue));
    }

    protected static String normalizeMetricDisplayName(String metricDisplayName) {
        return METRIC_DISPLAY_NAME_NORMALIZE_REPLACE_PATTERN.matcher(metricDisplayName).replaceAll(SdcctStringUtils.UNDERSCORE).toLowerCase();
    }

    protected static String buildMetricName(String ... metricNameParts) {
        return StringUtils.join(metricNameParts, SdcctStringUtils.PERIOD_CHAR);
    }
}
