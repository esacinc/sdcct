package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import com.sun.management.GarbageCollectorMXBean;
import com.sun.management.GcInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("metricSetGc")
public class GarbageCollectionMetricSet extends AbstractSdcctMetricSet {
    private final static String GC_METRIC_NAME_PART = "gc";

    private final static String AFTER_MEMORY_METRIC_NAME_PREFIX = buildMetricName(AFTER_METRIC_NAME_PART, MEMORY_METRIC_NAME_PART);
    private final static String BEFORE_MEMORY_METRIC_NAME_PREFIX = buildMetricName(BEFORE_METRIC_NAME_PART, MEMORY_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();

        ManagementFactory.getGarbageCollectorMXBeans().forEach(gcMxBean -> registerGcMetrics(metrics, jvmStartTime, ((GarbageCollectorMXBean) gcMxBean)));

        return metrics;
    }

    private static void registerGcMetrics(Map<String, Metric> metrics, long jvmStartTime, GarbageCollectorMXBean gcMxBean) {
        String gcName = gcMxBean.getName(), gcMetricNamePrefix = buildMetricName(GC_METRIC_NAME_PART, normalizeMetricDisplayName(gcName)),
            gcCollMetricNamePrefix = buildMetricName(gcMetricNamePrefix, COLLECTION_METRIC_NAME_PART);

        registerGauge(metrics, buildMetricName(gcMetricNamePrefix, NAME_METRIC_NAME_PART), gcName);
        registerGauge(metrics, buildMetricName(gcCollMetricNamePrefix, COUNT_METRIC_NAME_PART), gcMxBean.getCollectionCount());
        registerGauge(metrics, buildMetricName(gcCollMetricNamePrefix, TIME_METRIC_NAME_PART), gcMxBean.getCollectionTime());

        GcInfo gcInfo = gcMxBean.getLastGcInfo();

        if (gcInfo == null) {
            return;
        }

        String gcCollLastMetricNamePrefix = buildMetricName(gcCollMetricNamePrefix, LAST_METRIC_NAME_PART);

        registerGauge(metrics, buildMetricName(gcCollLastMetricNamePrefix, ID_METRIC_NAME_PART), gcInfo.getId());
        registerGauge(metrics, buildMetricName(gcCollLastMetricNamePrefix, TIME_START_METRIC_NAME_PREFIX), new Date((jvmStartTime + gcInfo.getStartTime())));
        registerGauge(metrics, buildMetricName(gcCollLastMetricNamePrefix, TIME_END_METRIC_NAME_PREFIX), new Date((jvmStartTime + gcInfo.getEndTime())));
        registerGauge(metrics, buildMetricName(gcCollLastMetricNamePrefix, DURATION_METRIC_NAME_PART), gcInfo.getDuration());

        registerGcMemoryUsageMetrics(metrics, buildMetricName(gcCollLastMetricNamePrefix, BEFORE_MEMORY_METRIC_NAME_PREFIX), gcInfo.getMemoryUsageBeforeGc());
        registerGcMemoryUsageMetrics(metrics, buildMetricName(gcCollLastMetricNamePrefix, AFTER_MEMORY_METRIC_NAME_PREFIX), gcInfo.getMemoryUsageAfterGc());
    }

    private static void registerGcMemoryUsageMetrics(Map<String, Metric> metrics, String metricNamePrefix, Map<String, MemoryUsage> gcMemUsages) {
        gcMemUsages.forEach((gcMemUsageName, gcMemUsage) -> registerMemoryUsageMetrics(metrics,
            buildMetricName(metricNamePrefix, normalizeMetricDisplayName(gcMemUsageName)), gcMemUsage));
    }
}
