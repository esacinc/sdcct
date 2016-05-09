package gov.hhs.onc.sdcct.data.db.jdbc.impl;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.metrics.MetricsTracker;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.PoolStats;
import gov.hhs.onc.sdcct.data.db.security.impl.DbUser;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.sql.Driver;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.annotation.Nonnegative;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

public class SdcctDataSourceConfig extends HikariConfig implements InitializingBean, MetricsTrackerFactory {
    private class SdcctMetricsTracker extends MetricsTracker {
        private PoolStats poolStats;
        private String connAcquireIntervalMetricName;
        private String connTimeoutRateMetricName;
        private String connUsageMetricName;
        private Map<String, Metric> metrics;

        public SdcctMetricsTracker(String poolName, PoolStats poolStats) {
            this.poolStats = poolStats;

            String metricNamePrefix = (POOL_METRIC_NAME_PREFIX + poolName), metricName;

            this.metrics =
                Stream.of(
                    new ImmutablePair<>((metricName = (metricNamePrefix + CONN_ACTIVE_METRIC_NAME_SUFFIX)), SdcctDataSourceConfig.this.metricRegistry.register(
                        metricName, ((Gauge<Integer>) () -> this.poolStats.getActiveConnections()))),
                    new ImmutablePair<>((this.connAcquireIntervalMetricName = (metricNamePrefix + CONN_ACQUIRE_INTERVAL_METRIC_NAME_SUFFIX)),
                        SdcctDataSourceConfig.this.metricRegistry.timer(this.connAcquireIntervalMetricName)),
                    new ImmutablePair<>((metricName = (metricNamePrefix + CONN_IDLE_METRIC_NAME_SUFFIX)), SdcctDataSourceConfig.this.metricRegistry.register(
                        metricName, ((Gauge<Integer>) () -> this.poolStats.getIdleConnections()))),
                    new ImmutablePair<>((metricName = (metricNamePrefix + CONN_PENDING_METRIC_NAME_SUFFIX)), SdcctDataSourceConfig.this.metricRegistry
                        .register(metricName, ((Gauge<Integer>) () -> this.poolStats.getPendingThreads()))),
                    new ImmutablePair<>((this.connTimeoutRateMetricName = (metricNamePrefix + CONN_TIMEOUT_RATE_METRIC_NAME_SUFFIX)),
                        SdcctDataSourceConfig.this.metricRegistry.meter(this.connTimeoutRateMetricName)),
                    new ImmutablePair<>((metricName = (metricNamePrefix + CONN_TOTAL_METRIC_NAME_SUFFIX)), SdcctDataSourceConfig.this.metricRegistry.register(
                        metricName, ((Gauge<Integer>) () -> this.poolStats.getTotalConnections()))),
                    new ImmutablePair<>((this.connUsageMetricName = (metricNamePrefix + CONN_USAGE_METRIC_NAME_SUFFIX)),
                        SdcctDataSourceConfig.this.metricRegistry.histogram(this.connUsageMetricName))).collect(SdcctStreamUtils.toMap(TreeMap::new));
        }

        @Override
        public void close() {
            this.metrics.keySet().forEach(SdcctDataSourceConfig.this.metricRegistry::remove);
        }

        @Override
        public void recordConnectionAcquiredNanos(long connAcquireIntervalNanos) {
            ((Timer) this.metrics.get(this.connAcquireIntervalMetricName)).update(connAcquireIntervalNanos, TimeUnit.NANOSECONDS);
        }

        @Override
        public void recordConnectionTimeout() {
            ((Meter) this.metrics.get(this.connTimeoutRateMetricName)).mark();
        }

        @Override
        public void recordConnectionUsageMillis(long connUsageMs) {
            ((Histogram) this.metrics.get(this.connUsageMetricName)).update(connUsageMs);
        }
    }

    private final static String POOL_METRIC_NAME_PREFIX = "data.db.source.pool.";

    private final static String CONN_METRIC_NAME_SUFFIX = ".connection.";
    private final static String CONN_ACQUIRE_INTERVAL_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "acquire.interval";
    private final static String CONN_ACTIVE_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "active";
    private final static String CONN_IDLE_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "idle";
    private final static String CONN_PENDING_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "pending";
    private final static String CONN_TIMEOUT_RATE_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "timeout.rate";
    private final static String CONN_TOTAL_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "total";
    private final static String CONN_USAGE_METRIC_NAME_SUFFIX = CONN_METRIC_NAME_SUFFIX + "usage";

    private final static String PASSWORD_PROP_NAME = "password";
    private final static String USER_PROP_NAME = "user";

    @Autowired
    private MetricRegistry metricRegistry;

    private Driver driver;
    private int loginTimeout;
    private String loginTimeoutPropName;
    private DbUser user;

    @Override
    public SdcctMetricsTracker create(String poolName, PoolStats poolStats) {
        return new SdcctMetricsTracker(poolName, poolStats);
    }

    public SdcctDriverDataSource buildDataSource(DbUser user, boolean singleConn) {
        Properties props = new Properties();
        props.putAll(this.getDataSourceProperties());
        props.put(this.loginTimeoutPropName, Integer.toString(this.loginTimeout));
        props.put(PASSWORD_PROP_NAME, user.getCredentials());
        props.put(USER_PROP_NAME, user.getName());

        return new SdcctDriverDataSource(this.driver, this.getJdbcUrl(), props, this.loginTimeoutPropName, singleConn);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.driver = ((Driver) Class.forName(this.getDriverClassName()).newInstance());

        this.setDataSource(this.buildDataSource(this.user, false));
    }

    public Driver getDriver() {
        return this.driver;
    }

    @Nonnegative
    public int getLoginTimeout() {
        return this.loginTimeout;
    }

    public void setLoginTimeout(@Nonnegative int loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    public String getLoginTimeoutPropertyName() {
        return this.loginTimeoutPropName;
    }

    public void setLoginTimeoutPropertyName(String loginTimeoutPropName) {
        this.loginTimeoutPropName = loginTimeoutPropName;
    }

    public DbUser getUser() {
        return this.user;
    }

    public void setUser(DbUser user) {
        this.user = user;
    }
}
