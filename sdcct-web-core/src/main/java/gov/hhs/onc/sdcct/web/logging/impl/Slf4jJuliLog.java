package gov.hhs.onc.sdcct.web.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.juli.logging.Log;
import org.slf4j.LoggerFactory;

public class Slf4jJuliLog implements Log {
    private final static String[] SKIP_CALLER_CLASS_NAMES = Stream.of(Slf4jJuliLog.class).map(Class::getName).toArray(String[]::new);

    private Logger logger;

    public Slf4jJuliLog() {
        this(StringUtils.EMPTY);
    }

    public Slf4jJuliLog(String name) {
        this.logger = ((Logger) LoggerFactory.getLogger(name));
    }

    @Override
    public boolean isFatalEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public void fatal(Object msg) {
        this.log(Level.ERROR, msg, null);
    }

    @Override
    public void fatal(Object msg, Throwable cause) {
        this.log(Level.ERROR, msg, cause);
    }

    @Override
    public void error(Object msg) {
        this.log(Level.ERROR, msg, null);
    }

    @Override
    public void error(Object msg, Throwable cause) {
        this.log(Level.ERROR, msg, cause);
    }

    @Override
    public void warn(Object msg) {
        this.log(Level.WARN, msg, null);
    }

    @Override
    public void warn(Object msg, Throwable cause) {
        this.log(Level.WARN, msg, cause);
    }

    @Override
    public void info(Object msg) {
        this.log(Level.INFO, msg, null);
    }

    @Override
    public void info(Object msg, Throwable cause) {
        this.log(Level.INFO, msg, cause);
    }

    @Override
    public void debug(Object msg) {
        this.log(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(Object msg, Throwable cause) {
        this.log(Level.DEBUG, msg, cause);
    }

    @Override
    public void trace(Object msg) {
        this.log(Level.TRACE, msg, null);
    }

    @Override
    public void trace(Object msg, Throwable cause) {
        this.log(Level.TRACE, msg, cause);
    }

    private void log(Level level, Object msg, @Nullable Throwable cause) {
        StackTraceElement[] callerFrames = new Throwable().getStackTrace();

        for (int a = 0; a < callerFrames.length; a++) {
            if (!StringUtils.startsWithAny(callerFrames[a].getClassName(), ((CharSequence[]) SKIP_CALLER_CLASS_NAMES))) {
                callerFrames = ArrayUtils.subarray(callerFrames, a, callerFrames.length);

                break;
            }
        }

        LoggingEvent event = new LoggingEvent(Logger.FQCN, this.logger, level, String.valueOf(msg), cause, null);
        event.setCallerData(callerFrames);

        this.logger.callAppenders(event);
    }
}
