package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import com.sun.management.UnixOperatingSystemMXBean;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.lang.management.ManagementFactory;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("metricSetSys")
public class SystemMetricSet extends AbstractSdcctMetricSet {
    private final static String CPU_METRIC_NAME_PART = "cpu";
    private final static String FILE_DESCRIPTOR_METRIC_NAME_PART = "file" + SdcctStringUtils.UNDERSCORE_CHAR + "descriptor";
    private final static String OS_METRIC_NAME_PART = "os";
    private final static String SYSTEM_METRIC_NAME_PART = "system";

    private final static String MEMORY_PHYSICAL_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, "physical");
    private final static String MEMORY_SWAP_METRIC_NAME_PREFIX = buildMetricName(MEMORY_METRIC_NAME_PART, "swap");

    private final static String CPU_PROCESSOR_COUNT_METRIC_NAME = buildMetricName(CPU_METRIC_NAME_PART, "processor", COUNT_METRIC_NAME_PART);
    private final static String CPU_LOAD_METRIC_NAME = buildMetricName(CPU_METRIC_NAME_PART, LOAD_METRIC_NAME_PART);
    private final static String CPU_LOAD_SYSTEM_METRIC_NAME = buildMetricName(CPU_LOAD_METRIC_NAME, SYSTEM_METRIC_NAME_PART);
    private final static String CPU_LOAD_SYSTEM_AVERAGE_METRIC_NAME = buildMetricName(CPU_LOAD_SYSTEM_METRIC_NAME, "average");
    private final static String CPU_TIME_METRIC_NAME = buildMetricName(CPU_METRIC_NAME_PART, TIME_METRIC_NAME_PART);
    private final static String FILE_DESCRIPTOR_OPEN_COUNT_METRIC_NAME = buildMetricName(FILE_DESCRIPTOR_METRIC_NAME_PART, "open", COUNT_METRIC_NAME_PART);
    private final static String FILE_DESCRIPTOR_MAX_COUNT_METRIC_NAME = buildMetricName(FILE_DESCRIPTOR_METRIC_NAME_PART, "max", COUNT_METRIC_NAME_PART);
    private final static String FILE_DESCRIPTOR_USAGE_METRIC_NAME = buildMetricName(FILE_DESCRIPTOR_METRIC_NAME_PART, USAGE_METRIC_NAME_PART);
    private final static String OS_ARCH_METRIC_NAME = buildMetricName(OS_METRIC_NAME_PART, "arch");
    private final static String OS_NAME_METRIC_NAME = buildMetricName(OS_METRIC_NAME_PART, NAME_METRIC_NAME_PART);
    private final static String OS_VERSION_METRIC_NAME = buildMetricName(OS_METRIC_NAME_PART, VERSION_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        UnixOperatingSystemMXBean osMxBean = ((UnixOperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());

        registerGauge(metrics, CPU_LOAD_METRIC_NAME, osMxBean.getProcessCpuLoad());
        registerGauge(metrics, CPU_LOAD_SYSTEM_METRIC_NAME, osMxBean.getSystemCpuLoad());
        registerGauge(metrics, CPU_LOAD_SYSTEM_AVERAGE_METRIC_NAME, osMxBean.getSystemLoadAverage());
        registerGauge(metrics, CPU_PROCESSOR_COUNT_METRIC_NAME, osMxBean.getAvailableProcessors());
        registerGauge(metrics, CPU_TIME_METRIC_NAME, osMxBean.getProcessCpuTime());
        registerGauge(metrics, OS_ARCH_METRIC_NAME, osMxBean.getArch());
        registerGauge(metrics, OS_NAME_METRIC_NAME, osMxBean.getName());
        registerGauge(metrics, OS_VERSION_METRIC_NAME, osMxBean.getVersion());

        long fileDescOpenCount = osMxBean.getOpenFileDescriptorCount(), fileDescMaxCount = osMxBean.getMaxFileDescriptorCount();

        registerGauge(metrics, FILE_DESCRIPTOR_OPEN_COUNT_METRIC_NAME, fileDescOpenCount);
        registerGauge(metrics, FILE_DESCRIPTOR_MAX_COUNT_METRIC_NAME, fileDescMaxCount);
        registerRatioGauge(metrics, FILE_DESCRIPTOR_USAGE_METRIC_NAME, fileDescOpenCount, fileDescMaxCount);

        registerMemoryUsageMetrics(metrics, MEMORY_PHYSICAL_METRIC_NAME_PREFIX, osMxBean.getFreePhysicalMemorySize(), -1,
            osMxBean.getTotalPhysicalMemorySize());
        registerMemoryUsageMetrics(metrics, MEMORY_SWAP_METRIC_NAME_PREFIX, osMxBean.getFreeSwapSpaceSize(), -1, osMxBean.getTotalSwapSpaceSize());

        return metrics;
    }
}
