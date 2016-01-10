package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component("metricSetThread")
public class ThreadMetricSet extends AbstractSdcctMetricSet {
    private final static String THREAD_DEADLOCKED_METRIC_NAME_PREFIX = buildMetricName(THREAD_METRIC_NAME_PART, "deadlocked");
    private final static String THREAD_STATE_METRIC_NAME_PREFIX = buildMetricName(THREAD_METRIC_NAME_PART, STATE_METRIC_NAME_PART);

    private final static String THREAD_COUNT_METRIC_NAME = buildMetricName(THREAD_METRIC_NAME_PART, COUNT_METRIC_NAME_PART);
    private final static String THREAD_DAEMON_COUNT_METRIC_NAME = buildMetricName(THREAD_METRIC_NAME_PART, "daemon", COUNT_METRIC_NAME_PART);
    private final static String THREAD_DEADLOCKED_COUNT_METRIC_NAME = buildMetricName(THREAD_DEADLOCKED_METRIC_NAME_PREFIX, COUNT_METRIC_NAME_PART);
    private final static String THREAD_DEADLOCKED_MONITOR_COUNT_METRIC_NAME =
        buildMetricName(THREAD_DEADLOCKED_METRIC_NAME_PREFIX, "monitor", COUNT_METRIC_NAME_PART);
    private final static String THREAD_DEADLOCKED_SYNCHRONIZER_COUNT_METRIC_NAME =
        buildMetricName(THREAD_DEADLOCKED_METRIC_NAME_PREFIX, "synchronizer", COUNT_METRIC_NAME_PART);
    private final static String THREAD_PEAK_COUNT_METRIC_NAME = buildMetricName(THREAD_METRIC_NAME_PART, PEAK_METRIC_NAME_PART, COUNT_METRIC_NAME_PART);
    private final static String THREAD_STARTED_COUNT_METRIC_NAME = buildMetricName(THREAD_METRIC_NAME_PART, STARTED_METRIC_NAME_PART, COUNT_METRIC_NAME_PART);
    private final static String THREAD_SUSPENDED_COUNT_METRIC_NAME = buildMetricName(THREAD_METRIC_NAME_PART, "suspended", COUNT_METRIC_NAME_PART);

    @Override
    public Map<String, Metric> getMetrics() {
        Map<String, Metric> metrics = super.getMetrics();
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();

        registerGauge(metrics, THREAD_COUNT_METRIC_NAME, threadMxBean.getThreadCount());
        registerGauge(metrics, THREAD_DAEMON_COUNT_METRIC_NAME, threadMxBean.getDaemonThreadCount());
        registerGauge(metrics, THREAD_PEAK_COUNT_METRIC_NAME, threadMxBean.getPeakThreadCount());
        registerGauge(metrics, THREAD_STARTED_COUNT_METRIC_NAME, threadMxBean.getTotalStartedThreadCount());

        if (threadMxBean.isObjectMonitorUsageSupported() && threadMxBean.isSynchronizerUsageSupported()) {
            long[] threadDeadlockedIds = threadMxBean.findDeadlockedThreads(), threadDeadlockedMonitorIds = threadMxBean.findMonitorDeadlockedThreads();
            int threadDeadlockedCount = ((threadDeadlockedIds != null) ? threadDeadlockedIds.length : -1),
                threadDeadlockedMonitorCount = ((threadDeadlockedMonitorIds != null) ? threadDeadlockedMonitorIds.length : -1);

            registerGauge(metrics, THREAD_DEADLOCKED_COUNT_METRIC_NAME, threadDeadlockedCount);
            registerGauge(metrics, THREAD_DEADLOCKED_MONITOR_COUNT_METRIC_NAME, threadDeadlockedMonitorCount);
            registerGauge(metrics, THREAD_DEADLOCKED_SYNCHRONIZER_COUNT_METRIC_NAME,
                (((threadDeadlockedCount >= 0) && (threadDeadlockedMonitorCount >= 0)) ? (threadDeadlockedCount - threadDeadlockedMonitorCount) : -1));
        }

        EnumMap<State, Integer> threadStates = new EnumMap<>(State.class);
        int threadSuspendedCount = 0;
        State threadState;

        for (ThreadInfo threadInfo : threadMxBean.getThreadInfo(threadMxBean.getAllThreadIds())) {
            if (threadInfo == null) {
                continue;
            }

            threadStates.put((threadState = threadInfo.getThreadState()), ((threadStates.containsKey(threadState) ? threadStates.get(threadState) : 0) + 1));

            if (threadInfo.isSuspended()) {
                threadSuspendedCount++;
            }
        }

        Stream.of(State.values()).forEach(threadStateItem -> registerGauge(metrics,
            buildMetricName(THREAD_STATE_METRIC_NAME_PREFIX, threadStateItem.name().toLowerCase()), threadStates.getOrDefault(threadStateItem, 0)));

        registerGauge(metrics, THREAD_SUSPENDED_COUNT_METRIC_NAME, threadSuspendedCount);

        return metrics;
    }
}
