package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("metricSetJvm")
public class JvmMetricSet extends AbstractSdcctMetricSet {
    private final static String RUNTIME_NAME_DELIM = "@";

    private final static String JVM_METRIC_NAME_PART = "j" + VM_METRIC_NAME_PART;

    private final static String JVM_SPEC_METRIC_NAME_PREFIX = buildMetricName(JVM_METRIC_NAME_PART, SPEC_METRIC_NAME_PART);
    private final static String JVM_VM_METRIC_NAME_PREFIX = buildMetricName(JVM_METRIC_NAME_PART, VM_METRIC_NAME_PART);

    private final static String JVM_HOST_METRIC_NAME = buildMetricName(JVM_METRIC_NAME_PART, HOST_METRIC_NAME_PART);
    private final static String JVM_MANAGEMENT_SPEC_VERSION_METRIC_NAME =
        buildMetricName(JVM_METRIC_NAME_PART, "management", SPEC_METRIC_NAME_PART, VERSION_METRIC_NAME_PART);
    private final static String JVM_NAME_METRIC_NAME = buildMetricName(JVM_METRIC_NAME_PART, NAME_METRIC_NAME_PART);
    private final static String JVM_PID_METRIC_NAME = buildMetricName(JVM_METRIC_NAME_PART, "pid");
    private final static String JVM_SPEC_NAME_METRIC_NAME = buildMetricName(JVM_SPEC_METRIC_NAME_PREFIX, NAME_METRIC_NAME_PART);
    private final static String JVM_SPEC_VENDOR_METRIC_NAME = buildMetricName(JVM_SPEC_METRIC_NAME_PREFIX, VENDOR_METRIC_NAME_PART);
    private final static String JVM_SPEC_VERSION_METRIC_NAME = buildMetricName(JVM_SPEC_METRIC_NAME_PREFIX, VERSION_METRIC_NAME_PART);
    private final static String JVM_TIME_START_METRIC_NAME = buildMetricName(JVM_METRIC_NAME_PART, TIME_START_METRIC_NAME_PREFIX);
    private final static String JVM_TIME_UP_METRIC_NAME = buildMetricName(JVM_METRIC_NAME_PART, TIME_UP_METRIC_NAME_PREFIX);
    private final static String JVM_VM_NAME_METRIC_NAME = buildMetricName(JVM_VM_METRIC_NAME_PREFIX, NAME_METRIC_NAME_PART);
    private final static String JVM_VM_VENDOR_METRIC_NAME = buildMetricName(JVM_VM_METRIC_NAME_PREFIX, VENDOR_METRIC_NAME_PART);
    private final static String JVM_VM_VERSION_METRIC_NAME = buildMetricName(JVM_VM_METRIC_NAME_PREFIX, VERSION_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        String runtimeName = runtimeMxBean.getName();
        String[] runtimeNameParts = StringUtils.split(runtimeName, RUNTIME_NAME_DELIM, 2);

        registerGauge(metrics, JVM_HOST_METRIC_NAME, runtimeNameParts[1]);
        registerGauge(metrics, JVM_MANAGEMENT_SPEC_VERSION_METRIC_NAME, runtimeMxBean.getManagementSpecVersion());
        registerGauge(metrics, JVM_NAME_METRIC_NAME, runtimeName);
        registerGauge(metrics, JVM_PID_METRIC_NAME, Long.parseLong(runtimeNameParts[0]));
        registerGauge(metrics, JVM_SPEC_NAME_METRIC_NAME, runtimeMxBean.getSpecName());
        registerGauge(metrics, JVM_SPEC_VENDOR_METRIC_NAME, runtimeMxBean.getSpecVendor());
        registerGauge(metrics, JVM_SPEC_VERSION_METRIC_NAME, runtimeMxBean.getSpecVersion());
        registerGauge(metrics, JVM_TIME_START_METRIC_NAME, new Date(runtimeMxBean.getStartTime()));
        registerGauge(metrics, JVM_TIME_UP_METRIC_NAME, runtimeMxBean.getUptime());
        registerGauge(metrics, JVM_VM_NAME_METRIC_NAME, runtimeMxBean.getVmName());
        registerGauge(metrics, JVM_VM_VENDOR_METRIC_NAME, runtimeMxBean.getVmVendor());
        registerGauge(metrics, JVM_VM_VERSION_METRIC_NAME, runtimeMxBean.getVmVersion());

        return metrics;
    }
}
