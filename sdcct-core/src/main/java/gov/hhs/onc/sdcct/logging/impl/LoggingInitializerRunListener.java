package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextAware;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.EncoderBase;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.factory.impl.AbstractSdcctBeanPostProcessor;
import gov.hhs.onc.sdcct.context.SdcctPropertyNames;
import gov.hhs.onc.sdcct.context.impl.AbstractApplicationInitializerRunListener;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.logging.AppenderType;
import gov.hhs.onc.sdcct.logging.LoggingInitializer;
import gov.hhs.onc.sdcct.logging.logstash.impl.SdcctLogstashEncoder;
import gov.hhs.onc.sdcct.utils.SdcctEnumUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.PropertySourceUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

@Order((Ordered.HIGHEST_PRECEDENCE + 1))
public class LoggingInitializerRunListener extends AbstractApplicationInitializerRunListener<LoggingInitializer> {
    @Order(Ordered.HIGHEST_PRECEDENCE)
    private class LoggerContextAwareBeanPostProcessor extends AbstractSdcctBeanPostProcessor<ContextAware> {
        public LoggerContextAwareBeanPostProcessor() {
            super(ContextAware.class);
        }

        @Override
        protected ContextAware postProcessBeforeInitializationInternal(ContextAware bean, String beanName) throws Exception {
            bean.setContext(LoggingInitializerRunListener.this.loggerContext);

            if (bean instanceof LoggerContextAware) {
                ((LoggerContextAware) bean).setLoggerContext(LoggingInitializerRunListener.this.loggerContext);
            }

            return bean;
        }
    }

    @Order((Ordered.HIGHEST_PRECEDENCE + 1))
    private class LogstashEncoderBeanPostProcessor extends AbstractSdcctBeanPostProcessor<SdcctLogstashEncoder> {
        public LogstashEncoderBeanPostProcessor() {
            super(SdcctLogstashEncoder.class);
        }

        @Override
        protected SdcctLogstashEncoder postProcessAfterInitializationInternal(SdcctLogstashEncoder bean, String beanName) throws Exception {
            ((CachingAppender) LoggingInitializerRunListener.this.appenders.get(AppenderType.LOGSTASH_FILE)).flush(AppenderType.LOGSTASH_FILE,
                LoggingInitializerRunListener.this.buildFileAppender(AppenderType.LOGSTASH_FILE, LoggingInitializerRunListener.this.buildLifeCycle(bean, true),
                    new File(LoggingInitializerRunListener.this.logFileDir,
                        (LoggingInitializerRunListener.this.app.getLogstashLogFileName() + FilenameUtils.EXTENSION_SEPARATOR + SdcctFileNameExtensions.JSON))));

            return bean;
        }
    }

    private class DefaultLoggingInitializer extends AbstractLoggingInitializer {
        public DefaultLoggingInitializer() {
            super(LoggingInitializerRunListener.this.app);
        }
    }

    private final static String CONSOLE_APPENDER_PATTERN = "%xT%" + TxMdcConverter.WORD + "{true}%" + PriorityColorCompositeConverter.WORD + " - %" +
        MessageMarkerConverter.WORD + "{" + AppenderType.CONSOLE.getId() + "}%n%" + RootCauseThrowableProxyConverter.WORD;

    private final static String FILE_APPENDER_PATTERN = "%d{yyyy-MM-dd HH:mm:ss z} [%C:%L %t] %" + TxMdcConverter.WORD + "%p - %" +
        MessageMarkerConverter.WORD + "{" + AppenderType.FILE.getId() + "}%n%" + RootCauseThrowableProxyConverter.WORD;

    private LoggerContext loggerContext;
    private Map<AppenderType, Appender<ILoggingEvent>> appenders = new EnumMap<>(AppenderType.class);
    private File logFileDir;

    public LoggingInitializerRunListener(SpringApplication app, String[] args) {
        super(LoggingInitializer.class, app, args);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext appContext) {
        ConfigurableBeanFactory beanFactory = appContext.getBeanFactory();
        beanFactory.addBeanPostProcessor(new LoggerContextAwareBeanPostProcessor());
        beanFactory.addBeanPostProcessor(new LogstashEncoderBeanPostProcessor());
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void environmentPrepared(ConfigurableEnvironment env) {
        this.buildInitializer(DefaultLoggingInitializer::new).initialize(env);

        this.loggerContext = ContextSelectorStaticBinder.getSingleton().getContextSelector().getDefaultLoggerContext();

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        this.loggerContext.stop();
        this.loggerContext.reset();

        LevelChangePropagator lvlChangePropagator = new LevelChangePropagator();
        lvlChangePropagator.setContext(this.loggerContext);
        lvlChangePropagator.setResetJUL(true);
        this.loggerContext.addListener(lvlChangePropagator);

        this.loggerContext.putObject(SdcctApplication.BEAN_NAME, this.app);

        this.buildConversionRule(MessageMarkerConverter.WORD, MessageMarkerConverter.class);
        this.buildConversionRule(PriorityColorCompositeConverter.WORD, PriorityColorCompositeConverter.class);
        this.buildConversionRule(RootCauseThrowableProxyConverter.WORD, RootCauseThrowableProxyConverter.class);
        this.buildConversionRule(ThreadSectionConverter.WORD, ThreadSectionConverter.class);
        this.buildConversionRule(TxMdcConverter.WORD, TxMdcConverter.class);

        this.buildAppender(new ConsoleAppender<>(), AppenderType.CONSOLE, this.buildPatternLayoutEncoder(CONSOLE_APPENDER_PATTERN), true);

        this.buildFileAppender(AppenderType.FILE, this.buildPatternLayoutEncoder(FILE_APPENDER_PATTERN), new File(
            (this.logFileDir = this.app.getLogFileDirectory()), (this.app.getLogFileName() + FilenameUtils.EXTENSION_SEPARATOR + SdcctFileNameExtensions.LOG)));

        this.buildCachingAppender(AppenderType.LOGSTASH_FILE);

        this.buildLogger(org.slf4j.Logger.ROOT_LOGGER_NAME, Level.WARN, true, this.appenders.values());

        PropertySourceUtils.getSubProperties(env.getPropertySources(), SdcctPropertyNames.LOGGING_LOGGER_PREFIX).forEach((loggerName, loggerPropValue) -> {
            String[] loggerPropValueParts = StringUtils.split(((String) loggerPropValue), SdcctStringUtils.COLON, 2);
            Level loggerLvl = Level.toLevel(loggerPropValueParts[0].toUpperCase(), null);

            if (loggerLvl == null) {
                throw new ApplicationContextException(
                    String.format("Unknown application (name=%s) logger (name=%s) level: %s", this.app.getName(), loggerName, loggerPropValueParts[0]));
            }

            this.buildLogger(loggerName, loggerLvl, false, ((loggerPropValueParts.length == 2)
                ? Stream.of(org.springframework.util.StringUtils.commaDelimitedListToStringArray(loggerPropValueParts[1])).map(loggerAppenderName -> {
                    AppenderType loggerAppenderType = SdcctEnumUtils.findById(AppenderType.class, loggerAppenderName);

                    if (!this.appenders.containsKey(loggerAppenderType)) {
                        throw new ApplicationContextException(String.format("Unknown application (name=%s) logger (name=%s) appender type (name=%s).",
                            this.app.getName(), loggerName, loggerAppenderName));
                    }

                    return this.appenders.get(loggerAppenderType);
                }).collect(Collectors.toList()) : this.appenders.values()));
        });

        StatusManager statusManager = this.loggerContext.getStatusManager();
        StatusUtil statusUtil = new StatusUtil(statusManager);
        long lastResetTime = statusUtil.timeOfLastReset();

        if (statusUtil.getHighestLevel(lastResetTime) >= Status.WARN) {
            StatusPrinter.print(statusManager, lastResetTime);

            throw new ApplicationContextException(String.format("Unable to initialize application (name=%s) logging.", this.app.getName()));
        }

        this.loggerContext.getLogger(LoggingInitializerRunListener.class).info(String.format("Application (name=%s) logging initialized.", this.app.getName()));
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private Logger buildLogger(String name, Level level, boolean additive, Collection<Appender<ILoggingEvent>> loggerAppenders) {
        Logger logger = this.loggerContext.getLogger(name);
        logger.setLevel(level);
        logger.setAdditive(additive);

        loggerAppenders.forEach(logger::addAppender);

        return logger;
    }

    private CachingAppender buildCachingAppender(AppenderType type) {
        return this.buildAppender(new CachingAppender(this.app), type, true);
    }

    private FileAppender<ILoggingEvent> buildFileAppender(AppenderType type, EncoderBase<ILoggingEvent> encoder, File file) {
        FileAppender<ILoggingEvent> appender = this.buildAppender(new FileAppender<>(), type, encoder, false);
        appender.setFile(file.getPath());
        appender.setPrudent(true);
        appender.start();

        return appender;
    }

    private <T extends OutputStreamAppender<ILoggingEvent>> T buildAppender(T appender, AppenderType type, EncoderBase<ILoggingEvent> encoder, boolean start) {
        this.buildAppender(appender, type, false).setEncoder(encoder);

        if (start) {
            appender.start();
        }

        return appender;
    }

    private <T extends UnsynchronizedAppenderBase<ILoggingEvent>> T buildAppender(T appender, AppenderType type, boolean start) {
        appender.setName(type.getId());

        this.appenders.put(type, appender);

        return this.buildLifeCycle(appender, start);
    }

    private PatternLayoutEncoder buildPatternLayoutEncoder(String pattern) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.setPattern(pattern);

        return this.buildLifeCycle(encoder, true);
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private void buildConversionRule(String word, Class<? extends Converter<ILoggingEvent>> clazz) {
        Map<String, String> reg = ((Map<String, String>) this.loggerContext.getObject(CoreConstants.PATTERN_RULE_REGISTRY));

        if (reg == null) {
            this.loggerContext.putObject(CoreConstants.PATTERN_RULE_REGISTRY, (reg = new HashMap<>()));
        }

        reg.put(word, clazz.getName());
    }

    private <T extends LifeCycle> T buildLifeCycle(T lifeCycle, boolean start) {
        if (lifeCycle instanceof ContextAware) {
            ((ContextAware) lifeCycle).setContext(this.loggerContext);
        }

        if (start) {
            lifeCycle.start();
        }

        return lifeCycle;
    }
}
