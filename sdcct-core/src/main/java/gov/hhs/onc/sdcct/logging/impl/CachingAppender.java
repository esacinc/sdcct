package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.InfoStatus;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.logging.AppenderType;
import java.util.Deque;
import java.util.LinkedList;

public class CachingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private final Object lock = new Object();

    private SdcctApplication app;
    private Deque<ILoggingEvent> cache = new LinkedList<>();
    private Appender<ILoggingEvent> appender;

    public CachingAppender(SdcctApplication app) {
        this.app = app;
    }

    public void flush(AppenderType appenderType, Appender<ILoggingEvent> appender) {
        synchronized (this.lock) {
            this.appender = appender;

            int numCachedEvents = this.cache.size();

            while (!this.cache.isEmpty()) {
                this.append(this.cache.removeFirst());
            }

            this.context.getStatusManager().add(new InfoStatus(String.format("Flushed %d cached application (name=%s) logging event(s) to appender (type=%s).",
                numCachedEvents, this.app.getName(), appenderType.getId()), appender));
        }
    }

    @Override
    public void stop() {
        synchronized (this.lock) {
            this.cache.clear();

            if (this.appender != null) {
                this.appender.stop();
            }

            super.stop();
        }
    }

    @Override
    public void start() {
        synchronized (this.lock) {
            super.start();
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        synchronized (this.lock) {
            if (!this.isStarted()) {
                return;
            }

            if (this.appender != null) {
                this.appender.doAppend(event);
            } else {
                this.cache.add(event);
            }
        }
    }
}
