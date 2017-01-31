package gov.hhs.onc.sdcct.xml.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import gov.hhs.onc.sdcct.api.SdcctIssueSeverity;
import gov.hhs.onc.sdcct.transform.location.impl.SdcctLocation;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctXmlReporter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("xmlReporterLogging")
public class LoggingXmlReporter extends AbstractSdcctXmlReporter<LoggingXmlReporter> {
    private final static Map<SdcctIssueSeverity, Level> SEVERITY_LEVELS = Stream
        .of(new ImmutablePair<>(SdcctIssueSeverity.INFORMATION, Level.INFO), new ImmutablePair<>(SdcctIssueSeverity.WARNING, Level.WARN),
            new ImmutablePair<>(SdcctIssueSeverity.ERROR, Level.ERROR), new ImmutablePair<>(SdcctIssueSeverity.FATAL, Level.ERROR))
        .collect(SdcctStreamUtils.toMap());

    private final static Set<String> STACK_SKIP_CLASS_NAMES =
        new HashSet<>(ClassUtils.convertClassesToClassNames(Arrays.asList(AbstractSdcctXmlReporter.class, LoggingXmlReporter.class)));

    private final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(LoggingXmlReporter.class));

    public LoggingXmlReporter() {
        super();
    }

    @Override
    public void report(SdcctIssueSeverity severity, String msg, SdcctLocation loc, @Nullable Throwable cause) {
        LoggingEvent event = new LoggingEvent(Logger.FQCN, LOGGER, SEVERITY_LEVELS.get(severity), msg, cause, null);
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

    @Override
    protected LoggingXmlReporter cloneInternal() {
        return new LoggingXmlReporter();
    }
}
