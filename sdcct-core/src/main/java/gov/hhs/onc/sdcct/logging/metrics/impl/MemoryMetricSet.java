package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("metricSetMem")
public class MemoryMetricSet extends AbstractSdcctMetricSet {
    private final static String MEMORY_BUFFER_POOL_METRIC_NAME_PREFIX =
        buildMetricName(MEMORY_METRIC_NAME_PART, ("buffer" + METRIC_NAME_PART_DELIM + POOL_METRIC_NAME_PART));
    private final static String MEMORY_HEAP_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, HEAP_METRIC_NAME_PART);
    private final static String MEMORY_NON_HEAP_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, ("non" + HEAP_METRIC_NAME_PART));
    private final static String MEMORY_POOL_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, POOL_METRIC_NAME_PART);
    private final static String MEMORY_TOTAL_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, TOTAL_METRIC_NAME_PART);

    private final static String MEMORY_OBJ_PENDING_FINALIZATION_COUNT_METRIC_NAME = buildMetricName(MEMORY_METRIC_NAME_PART,
        ("obj" + METRIC_NAME_PART_DELIM + "pending" + METRIC_NAME_PART_DELIM + "finalization"), COUNT_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        MemoryMXBean memMxBean = ManagementFactory.getMemoryMXBean();

        registerGauge(metrics, MEMORY_OBJ_PENDING_FINALIZATION_COUNT_METRIC_NAME, memMxBean.getObjectPendingFinalizationCount());

        MemoryUsage heapMemUsage = memMxBean.getHeapMemoryUsage(), nonHeapMemUsage = memMxBean.getNonHeapMemoryUsage();

        registerMemoryUsageMetrics(metrics, MEMORY_HEAP_METRIC_NAME_PREFIX, heapMemUsage);
        registerMemoryUsageMetrics(metrics, MEMORY_NON_HEAP_METRIC_NAME_PREFIX, nonHeapMemUsage);
        registerMemoryUsageMetrics(metrics, MEMORY_TOTAL_METRIC_NAME_PREFIX, (heapMemUsage.getCommitted() + nonHeapMemUsage.getCommitted()),
            (heapMemUsage.getInit() + nonHeapMemUsage.getInit()), -1, (heapMemUsage.getUsed() + nonHeapMemUsage.getUsed()),
            (heapMemUsage.getMax() + nonHeapMemUsage.getMax()));

        ManagementFactory.getMemoryPoolMXBeans().stream().filter(MemoryPoolMXBean::isValid)
            .forEach(memPoolMxBean -> registerMemoryPoolMetrics(metrics, memPoolMxBean));

        ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class).stream().forEach(bufferPoolMxBean -> registerBufferPoolMetrics(metrics, bufferPoolMxBean));

        return metrics;
    }

    private static void registerBufferPoolMetrics(Map<String, Metric> metrics, BufferPoolMXBean bufferPoolMxBean) {
        String bufferPoolName = bufferPoolMxBean.getName(),
            bufferPoolMetricNamePrefix = buildMetricName(MEMORY_BUFFER_POOL_METRIC_NAME_PREFIX, normalizeMetricDisplayName(bufferPoolName));

        registerGauge(metrics, buildMetricName(bufferPoolMetricNamePrefix, NAME_METRIC_NAME_PART), bufferPoolName);
        registerGauge(metrics, buildMetricName(bufferPoolMetricNamePrefix, CAPACITY_METRIC_NAME_PART), bufferPoolMxBean.getTotalCapacity());
        registerGauge(metrics, buildMetricName(bufferPoolMetricNamePrefix, COUNT_METRIC_NAME_PART), bufferPoolMxBean.getCount());
        registerGauge(metrics, buildMetricName(bufferPoolMetricNamePrefix, USED_METRIC_NAME_PART), bufferPoolMxBean.getMemoryUsed());
    }

    private static void registerMemoryPoolMetrics(Map<String, Metric> metrics, MemoryPoolMXBean memPoolMxBean) {
        String memPoolName = memPoolMxBean.getName(),
            memPoolMetricNamePrefix = buildMetricName(MEMORY_POOL_METRIC_NAME_PREFIX, normalizeMetricDisplayName(memPoolName));

        registerGauge(metrics, buildMetricName(memPoolMetricNamePrefix, NAME_METRIC_NAME_PART), memPoolName);
        registerGauge(metrics, buildMetricName(memPoolMetricNamePrefix, TYPE_METRIC_NAME_PART), memPoolMxBean.getType().name());

        registerMemoryUsageMetrics(metrics, memPoolMetricNamePrefix, memPoolMxBean.getUsage());
        registerMemoryUsageMetrics(metrics, buildMetricName(memPoolMetricNamePrefix, PEAK_METRIC_NAME_PART), memPoolMxBean.getPeakUsage());
        registerMemoryUsageMetrics(metrics, buildMetricName(memPoolMetricNamePrefix, COLLECTION_METRIC_NAME_PART), memPoolMxBean.getCollectionUsage());

        if (memPoolMxBean.isUsageThresholdSupported()) {
            String memPoolThresholdMetricName = buildMetricName(memPoolMetricNamePrefix, THRESHOLD_METRIC_NAME_PART),
                memPoolThresholdExceededMetricName = buildMetricName(memPoolThresholdMetricName, EXCEEDED_METRIC_NAME_PART);

            registerGauge(metrics, memPoolThresholdMetricName, memPoolMxBean.getUsageThreshold());
            registerGauge(metrics, memPoolThresholdExceededMetricName, memPoolMxBean.isUsageThresholdExceeded());
            registerGauge(metrics, buildMetricName(memPoolThresholdExceededMetricName, COUNT_METRIC_NAME_PART), memPoolMxBean.getUsageThresholdCount());
        }

        if (memPoolMxBean.isCollectionUsageThresholdSupported()) {
            String memPoolCollThresholdMetricName = buildMetricName(memPoolMetricNamePrefix, COLLECTION_METRIC_NAME_PART, THRESHOLD_METRIC_NAME_PART),
                memPoolCollThresholdExceededMetricName = buildMetricName(memPoolCollThresholdMetricName, EXCEEDED_METRIC_NAME_PART);

            registerGauge(metrics, memPoolCollThresholdMetricName, memPoolMxBean.getCollectionUsageThreshold());
            registerGauge(metrics, memPoolCollThresholdExceededMetricName, memPoolMxBean.isCollectionUsageThresholdExceeded());
            registerGauge(metrics, buildMetricName(memPoolCollThresholdExceededMetricName, COUNT_METRIC_NAME_PART),
                memPoolMxBean.getCollectionUsageThresholdCount());
        }
    }
}
