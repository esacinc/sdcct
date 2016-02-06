package gov.hhs.onc.sdcct.logging.metrics.impl;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Reporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.sdcct.logging.impl.MarkerBuilder;
import gov.hhs.onc.sdcct.logging.logstash.LogstashTags;
import gov.hhs.onc.sdcct.logging.metrics.SdcctMetricSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.Nonnegative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class LogstashReporter implements Reporter, SmartLifecycle {
    private class ReportMetricsTask implements Runnable {
        @Override
        public void run() {
            // noinspection SynchronizeOnNonFinalField
            synchronized (LogstashReporter.this.metricRegistry) {
                Map<String, Metric> transientMetrics = new HashMap<>();

                try {
                    LogstashReporter.this.transientMetricSets
                        .forEach(transientMetricSet -> transientMetricSet.getMetrics().forEach((transientMetricName, transientMetric) -> {
                            transientMetrics.put(transientMetricName, transientMetric);

                            LogstashReporter.this.metricRegistry.register(transientMetricName, transientMetric);
                        }));

                    LOGGER.info(
                        new MarkerBuilder(LogstashTags.METRICS)
                            .appendField(METRICS_MARKER_FIELD_NAME, LogstashReporter.this.objMapper.valueToTree(LogstashReporter.this.metricRegistry)).build(),
                        String.format("Reporting %d metric(s).", LogstashReporter.this.metricRegistry.getMetrics().size()));
                } catch (Exception e) {
                    LOGGER.error(String.format("Unable to report %d metric(s).", LogstashReporter.this.metricRegistry.getMetrics().size()), e);
                } finally {
                    transientMetrics.keySet().forEach(LogstashReporter.this.metricRegistry::remove);
                }
            }
        }
    }

    private final static String METRICS_MARKER_FIELD_NAME = "metrics";

    private final static Logger LOGGER = LoggerFactory.getLogger(LogstashReporter.class);

    @Autowired
    private List<SdcctMetricSet> transientMetricSets;

    @Autowired
    private MetricRegistry metricRegistry;

    private ObjectMapper objMapper;
    private long period;
    private ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> taskFuture;

    @Override
    public void stop(Runnable callback) {
        this.stop();

        callback.run();
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            this.taskFuture.cancel(true);
        }
    }

    @Override
    public void start() {
        if (!this.isRunning()) {
            this.taskFuture = this.taskScheduler.scheduleAtFixedRate(new ReportMetricsTask(), this.period);
        }
    }

    @Override
    public boolean isRunning() {
        return ((this.taskFuture != null) && !this.taskFuture.isDone());
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    public ObjectMapper getObjectMapper() {
        return this.objMapper;
    }

    public void setObjectMapper(ObjectMapper objMapper) {
        this.objMapper = objMapper;
    }

    @Nonnegative
    public long getPeriod() {
        return this.period;
    }

    public void setPeriod(@Nonnegative long period) {
        this.period = period;
    }

    @Override
    public int getPhase() {
        return 0;
    }

    public ThreadPoolTaskScheduler getTaskScheduler() {
        return this.taskScheduler;
    }

    public void setTaskScheduler(ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }
}
