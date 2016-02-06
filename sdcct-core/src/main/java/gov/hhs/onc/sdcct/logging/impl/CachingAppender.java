package gov.hhs.onc.sdcct.logging.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.status.InfoStatus;
import gov.hhs.onc.sdcct.context.impl.SdcctApplication;
import gov.hhs.onc.sdcct.logging.AppenderType;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CachingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private SdcctApplication app;
    private final Lock lock = new ReentrantLock();
    private Deque<ILoggingEvent> cache = new LinkedList<>();
    private Appender<ILoggingEvent> appender;

    public CachingAppender(SdcctApplication app) {
        this.app = app;
    }

    public void flush(AppenderType appenderType, Appender<ILoggingEvent> appender) {
        this.lock.lock();

        try {
            this.appender = appender;

            int numCachedEvents = this.cache.size();

            while (!this.cache.isEmpty()) {
                this.doAppend(this.cache.removeFirst());
            }

            this.context.getStatusManager().add(new InfoStatus(String.format("Flushed %d cached application (name=%s) logging event(s) to appender (type=%s).",
                numCachedEvents, this.app.getName(), appenderType.getId()), appender));
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void stop() {
        this.lock.lock();

        try {
            this.cache.clear();

            if (this.appender != null) {
                this.appender.stop();
            }

            super.stop();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public void start() {
        this.lock.lock();

        try {
            super.start();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    protected void append(ILoggingEvent event) {
        this.lock.lock();

        try {
            if (!this.isStarted()) {
                return;
            }

            if (this.appender != null) {
                this.appender.doAppend(event);
            } else {
                this.cache.add(event);
            }
        } finally {
            this.lock.unlock();
        }
    }
}
