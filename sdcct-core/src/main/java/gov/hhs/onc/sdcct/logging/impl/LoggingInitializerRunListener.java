package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.EncoderBase;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.context.SdcctProperties;
import gov.hhs.onc.sdcct.context.impl.AbstractApplicationInitializerRunListener;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.logging.LoggingInitializer;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.zone.ZoneOffsetTransitionRule.TimeDefinition;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.logstash.logback.composite.LogstashVersionJsonProvider;
import net.logstash.logback.composite.loggingevent.CallerDataJsonProvider;
import net.logstash.logback.composite.loggingevent.LogLevelJsonProvider;
import net.logstash.logback.composite.loggingevent.LogLevelValueJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggerNameJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggingEventFormattedTimestampJsonProvider;
import net.logstash.logback.composite.loggingevent.LoggingEventJsonProviders;
import net.logstash.logback.composite.loggingevent.LogstashMarkersJsonProvider;
import net.logstash.logback.composite.loggingevent.MessageJsonProvider;
import net.logstash.logback.composite.loggingevent.StackTraceJsonProvider;
import net.logstash.logback.composite.loggingevent.TagsJsonProvider;
import net.logstash.logback.composite.loggingevent.ThreadNameJsonProvider;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LoggingInitializerRunListener extends AbstractApplicationInitializerRunListener<LoggingInitializer> {
    private class DefaultLoggingInitializer extends AbstractLoggingInitializer {
        public DefaultLoggingInitializer() {
            super(LoggingInitializerRunListener.this.app);
        }
    }

    private final static String CONSOLE_APPENDER_NAME = "console";
    private final static String CONSOLE_APPENDER_PATTERN = "%" + PriorityColorCompositeConverter.WORD + " - %m%n%" + RootCauseThrowableProxyConverter.WORD;

    private final static String FILE_APPENDER_NAME = "file";
    private final static String FILE_APPENDER_PATTERN = "%d{yyyy-MM-dd HH:mm:ss z} [%C:%L %t] %p - %m%n%" + RootCauseThrowableProxyConverter.WORD;
    private final static String FILE_APPENDER_FILE_NAME_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR + "log";

    private final static String LOGSTASH_FILE_APPENDER_NAME = "logstashFile";
    private final static String LOGSTASH_FILE_APPENDER_FILE_NAME_SUFFIX = FilenameUtils.EXTENSION_SEPARATOR + "json";

    private final static String LOGGER_PROP_VALUE_DELIM = ":";

    public LoggingInitializerRunListener(SpringApplication app, String[] args) {
        super(LoggingInitializer.class, app, args);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void environmentPrepared(ConfigurableEnvironment env) {
        this.buildInitializer(DefaultLoggingInitializer::new).initialize(env);

        LoggerContext context = ContextSelectorStaticBinder.getSingleton().getContextSelector().getDefaultLoggerContext();

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        context.stop();
        context.reset();

        LevelChangePropagator lvlChangePropagator = new LevelChangePropagator();
        lvlChangePropagator.setContext(context);
        lvlChangePropagator.setResetJUL(true);
        context.addListener(lvlChangePropagator);

        context.putObject(SdcctApplication.BEAN_NAME, this.app);

        buildConversionRule(context, PriorityColorCompositeConverter.WORD, PriorityColorCompositeConverter.class);
        buildConversionRule(context, RootCauseThrowableProxyConverter.WORD, RootCauseThrowableProxyConverter.class);

        Map<String, Appender<ILoggingEvent>> appenders = new LinkedHashMap<>(3);

        buildAppender(context, appenders, new ConsoleAppender<>(), CONSOLE_APPENDER_NAME, buildPatternLayoutEncoder(context, CONSOLE_APPENDER_PATTERN), true);

        File logFileDir = this.app.getLogFileDirectory();

        buildFileAppender(context, appenders, FILE_APPENDER_NAME, buildPatternLayoutEncoder(context, FILE_APPENDER_PATTERN),
            new File(logFileDir, (this.app.getLogFileName() + FILE_APPENDER_FILE_NAME_SUFFIX)));

        LoggingEventJsonProviders logstashJsonProvs = new LoggingEventJsonProviders();
        logstashJsonProvs.addVersion(new LogstashVersionJsonProvider<>());
        logstashJsonProvs.addCallerData(new CallerDataJsonProvider());
        logstashJsonProvs.addLoggerName(new LoggerNameJsonProvider());
        logstashJsonProvs.addLogLevel(new LogLevelJsonProvider());
        logstashJsonProvs.addLogLevelValue(new LogLevelValueJsonProvider());
        logstashJsonProvs.addLogstashMarkers(new LogstashMarkersJsonProvider());
        logstashJsonProvs.addMessage(new MessageJsonProvider());
        logstashJsonProvs.addTags(new TagsJsonProvider());
        logstashJsonProvs.addThreadName(new ThreadNameJsonProvider());

        LoggingEventFormattedTimestampJsonProvider logstashTimestampJsonProv = new LoggingEventFormattedTimestampJsonProvider();
        logstashTimestampJsonProv.setTimeZone(TimeDefinition.UTC.name());
        logstashJsonProvs.addTimestamp(logstashTimestampJsonProv);

        StackTraceJsonProvider logstashStackTraceJsonProv = new StackTraceJsonProvider();
        logstashStackTraceJsonProv.setThrowableConverter(buildLifeCycle(context, new RootCauseThrowableProxyConverter(), true));
        logstashJsonProvs.addStackTrace(logstashStackTraceJsonProv);

        LoggingEventCompositeJsonEncoder logstashFileEnc = new LoggingEventCompositeJsonEncoder();
        logstashFileEnc.setEncoding(StandardCharsets.UTF_8.name());
        logstashFileEnc.setLineSeparator(StringUtils.LF);
        logstashFileEnc.setProviders(logstashJsonProvs);

        buildFileAppender(context, appenders, LOGSTASH_FILE_APPENDER_NAME, buildLifeCycle(context, logstashFileEnc, true),
            new File(logFileDir, (this.app.getLogstashLogFileName() + LOGSTASH_FILE_APPENDER_FILE_NAME_SUFFIX)));

        buildLogger(context, org.slf4j.Logger.ROOT_LOGGER_NAME, Level.WARN, true, appenders.values());

        PropertySourceUtils.getSubProperties(env.getPropertySources(), SdcctProperties.LOGGING_LOGGER_PREFIX).forEach((loggerName, loggerPropValue) -> {
            String[] loggerPropValueParts = StringUtils.split(((String) loggerPropValue), LOGGER_PROP_VALUE_DELIM, 2);
            Level loggerLvl = Level.toLevel(loggerPropValueParts[0].toUpperCase(), null);

            if (loggerLvl == null) {
                throw new ApplicationContextException(
                    String.format("Unknown application (name=%s) logger (name=%s) level: %s", this.app.getName(), loggerName, loggerPropValueParts[0]));
            }

            buildLogger(context, loggerName, loggerLvl, false, ((loggerPropValueParts.length == 2)
                ? Stream.of(org.springframework.util.StringUtils.commaDelimitedListToStringArray(loggerPropValueParts[1])).map(loggerAppenderName -> {
                if (!appenders.containsKey(loggerAppenderName)) {
                    throw new ApplicationContextException(String.format("Unknown application (name=%s) logger (name=%s) appender (name=%s).",
                        this.app.getName(), loggerName, loggerAppenderName));
                }

                return appenders.get(loggerAppenderName);
            }).collect(Collectors.toList()) : appenders.values()));
        });

        StatusManager statusManager = context.getStatusManager();
        StatusUtil statusUtil = new StatusUtil(statusManager);
        long lastResetTime = statusUtil.timeOfLastReset();

        if (statusUtil.getHighestLevel(lastResetTime) > Status.WARN) {
            StatusPrinter.print(statusManager, lastResetTime);

            throw new ApplicationContextException(String.format("Unable to initialize application (name=%s) logging.", this.app.getName()));
        }

        context.getLogger(LoggingInitializerRunListener.class).info(String.format("Application (name=%s) logging initialized.", this.app.getName()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private static Logger buildLogger(LoggerContext context, String name, Level level, boolean additive, Collection<Appender<ILoggingEvent>> appenders) {
        Logger logger = context.getLogger(name);
        logger.setLevel(level);
        logger.setAdditive(additive);

        appenders.forEach(logger::addAppender);

        return logger;
    }

    private static FileAppender<ILoggingEvent> buildFileAppender(LoggerContext context, Map<String, Appender<ILoggingEvent>> appenders, String name,
        EncoderBase<ILoggingEvent> enc, File file) {
        FileAppender<ILoggingEvent> appender = buildAppender(context, appenders, new FileAppender<>(), name, enc, false);
        appender.setFile(file.getPath());
        appender.setPrudent(true);
        appender.start();

        return appender;
    }

    private static <T extends OutputStreamAppender<ILoggingEvent>> T buildAppender(LoggerContext context, Map<String, Appender<ILoggingEvent>> appenders,
        T appender, String name, EncoderBase<ILoggingEvent> enc, boolean start) {
        appender.setName(name);
        appender.setEncoder(enc);

        appenders.put(name, appender);

        return buildLifeCycle(context, appender, start);
    }

    private static PatternLayoutEncoder buildPatternLayoutEncoder(LoggerContext context, String pattern) {
        PatternLayoutEncoder enc = new PatternLayoutEncoder();
        enc.setCharset(StandardCharsets.UTF_8);
        enc.setPattern(pattern);

        return buildLifeCycle(context, enc, true);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private static void buildConversionRule(LoggerContext context, String word, Class<? extends Converter<ILoggingEvent>> clazz) {
        Map<String, String> reg = ((Map<String, String>) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY));

        if (reg == null) {
            context.putObject(CoreConstants.PATTERN_RULE_REGISTRY, (reg = new HashMap<>()));
        }

        reg.put(word, clazz.getName());
    }

    private static <T extends LifeCycle> T buildLifeCycle(LoggerContext context, T lifeCycle, boolean start) {
        if (lifeCycle instanceof ContextAware) {
            ((ContextAware) lifeCycle).setContext(context);
        }

        if (start) {
            lifeCycle.start();
        }

        return lifeCycle;
    }
}
