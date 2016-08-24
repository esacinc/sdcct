package gov.hhs.onc.sdcct.xml.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.impl.SdcctLocation;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("xmlReporterLogging")
public class LoggingXmlReporter extends AbstractSdcctXmlReporter<LoggingXmlReporter> {
    private static class XmlReporterLogger extends net.sf.saxon.lib.Logger {
        public final static XmlReporterLogger INSTANCE = new XmlReporterLogger();

        private final static Map<Integer, Level> SEVERITY_LEVELS = Stream.of(new ImmutablePair<>(INFO, Level.INFO), new ImmutablePair<>(WARNING, Level.WARN),
            new ImmutablePair<>(ERROR, Level.ERROR), new ImmutablePair<>(DISASTER, Level.ERROR)).collect(SdcctStreamUtils.toMap());

        private final static Set<String> STACK_SKIP_CLASS_NAMES =
            new HashSet<>(ClassUtils.convertClassesToClassNames(Arrays.asList(Logger.class, LoggingXmlReporter.class, XmlReporterLogger.class)));

        private final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(XmlReporterLogger.class));

        @Override
        public void disaster(@Nullable String msg) {
            this.disaster(msg, null);
        }

        public void disaster(@Nullable String msg, @Nullable Throwable cause) {
            this.error(msg, cause);
        }

        @Override
        public void error(@Nullable String msg) {
            this.error(msg, null);
        }

        public void error(@Nullable String msg, @Nullable Throwable cause) {
            this.log(Level.ERROR, msg, cause);
        }

        @Override
        public void warning(@Nullable String msg) {
            this.warning(msg, null);
        }

        public void warning(@Nullable String msg, @Nullable Throwable cause) {
            this.log(Level.WARN, msg, cause);
        }

        @Override
        public void info(@Nullable String msg) {
            this.info(msg, null);
        }

        public void info(@Nullable String msg, @Nullable Throwable cause) {
            this.log(Level.INFO, msg, cause);
        }

        public void debug(@Nullable String msg) {
            this.debug(msg, null);
        }

        public void debug(@Nullable String msg, @Nullable Throwable cause) {
            this.log(Level.DEBUG, msg, cause);
        }

        public void trace(@Nullable String msg) {
            this.trace(msg, null);
        }

        public void trace(@Nullable String msg, @Nullable Throwable cause) {
            this.log(Level.TRACE, msg, cause);
        }

        @Override
        public StreamResult asStreamResult() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void println(String msg, int severity) {
            this.log(SEVERITY_LEVELS.get(severity), msg, null);
        }

        public void log(Level level, @Nullable String msg, @Nullable Throwable cause) {
            LoggingEvent event = new LoggingEvent(Logger.FQCN, LOGGER, level, msg, cause, null);

            StackTraceElement[] stackFrames = new Throwable().getStackTrace();

            for (int a = 1; a < stackFrames.length; a++) {
                if (!STACK_SKIP_CLASS_NAMES.contains(stackFrames[a].getClassName())) {
                    stackFrames = ArrayUtils.subarray(stackFrames, a, stackFrames.length);

                    break;
                }
            }

            event.setCallerData(stackFrames);

            LOGGER.callAppenders(event);
        }
    }

    private final static Map<SdcctIssueSeverity, Level> ISSUE_SEVERITY_LEVELS = Stream
        .of(new ImmutablePair<>(SdcctIssueSeverity.INFORMATION, Level.INFO), new ImmutablePair<>(SdcctIssueSeverity.WARNING, Level.WARN),
            new ImmutablePair<>(SdcctIssueSeverity.ERROR, Level.ERROR), new ImmutablePair<>(SdcctIssueSeverity.FATAL, Level.ERROR))
        .collect(SdcctStreamUtils.toMap());

    public LoggingXmlReporter() {
        super(XmlReporterLogger.INSTANCE);
    }

    @Override
    public void report(SdcctIssueSeverity severity, String msg, SdcctLocation loc, @Nullable Throwable cause) {
        XmlReporterLogger.INSTANCE.log(ISSUE_SEVERITY_LEVELS.get(severity),
            String.format("XML event (lineNum=%d, columnNum=%d, elemQname=%s, attrQname=%s): %s", loc.getLineNumber(), loc.getColumnNumber(),
                loc.getAttributeQname(), loc.getElementQname(), msg),
            cause);
    }

    @Override
    protected LoggingXmlReporter cloneInternal() {
        return new LoggingXmlReporter();
    }
}
