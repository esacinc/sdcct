package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("metricSetClassLoading")
public class ClassLoadingMetricSet extends AbstractSdcctMetricSet {
    private final static String CLASSES_METRIC_NAME_PART = "classes";
    private final static String LOADED_METRIC_NAME_PART = "loaded";

    private final static String CLASSES_LOADED_METRIC_NAME_PREFIX = buildMetricName(CLASSES_METRIC_NAME_PART, LOADED_METRIC_NAME_PART);

    private final static String CLASSES_LOADED_COUNT_METRIC_NAME = buildMetricName(CLASSES_LOADED_METRIC_NAME_PREFIX, COUNT_METRIC_NAME_PART);
    private final static String CLASSES_LOADED_TOTAL_COUNT_METRIC_NAME =
        buildMetricName(CLASSES_LOADED_METRIC_NAME_PREFIX, TOTAL_METRIC_NAME_PART, COUNT_METRIC_NAME_PART);
    private final static String CLASSES_UNLOADED_COUNT_METRIC_NAME =
        buildMetricName(CLASSES_METRIC_NAME_PART, ("un" + LOADED_METRIC_NAME_PART), COUNT_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        ClassLoadingMXBean classLoadingMxBean = ManagementFactory.getClassLoadingMXBean();

        registerGauge(metrics, CLASSES_LOADED_COUNT_METRIC_NAME, classLoadingMxBean.getLoadedClassCount());
        registerGauge(metrics, CLASSES_LOADED_TOTAL_COUNT_METRIC_NAME, classLoadingMxBean.getTotalLoadedClassCount());
        registerGauge(metrics, CLASSES_UNLOADED_COUNT_METRIC_NAME, classLoadingMxBean.getUnloadedClassCount());

        return metrics;
    }
}
