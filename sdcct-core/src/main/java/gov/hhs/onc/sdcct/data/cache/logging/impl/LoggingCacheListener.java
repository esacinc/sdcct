package gov.hhs.onc.sdcct.data.cache.logging.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheEventListenerAdapter;
import net.sf.ehcache.event.RegisteredEventListeners;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("cacheListenerLogging")
public class LoggingCacheListener extends CacheEventListenerAdapter {
    private static enum CacheEventType implements IdentifiedBean {
        EVICTED, EXPIRED, PUT, UPDATED, REMOVED;

        private final String id;

        private CacheEventType() {
            this.id = this.name().toLowerCase();
        }

        @Override
        public String getId() {
            return this.id;
        }
    }

    private final static String MSG_FORMAT = "Cache (name=%s, status=%s, approxSize=%d) element (key=%s) %s.";

    private final static Set<String> STACK_SKIP_CLASS_NAMES =
        new HashSet<>(ClassUtils.convertClassesToClassNames(Arrays.asList(LoggingCacheListener.class, RegisteredEventListeners.class)));

    private final static Logger LOGGER = ((Logger) LoggerFactory.getLogger(LoggingCacheListener.class));

    @Override
    public void notifyElementRemoved(Ehcache cache, Element elem) throws CacheException {
        logEvent(cache, elem, CacheEventType.REMOVED);
    }

    @Override
    public void notifyElementEvicted(Ehcache cache, Element elem) {
        logEvent(cache, elem, CacheEventType.EVICTED);
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element elem) {
        logEvent(cache, elem, CacheEventType.EXPIRED);
    }

    @Override
    public void notifyElementUpdated(Ehcache cache, Element elem) throws CacheException {
        logEvent(cache, elem, CacheEventType.UPDATED);
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element elem) throws CacheException {
        logEvent(cache, elem, CacheEventType.PUT);
    }

    private static void logEvent(Ehcache cache, Element elem, CacheEventType eventType) {
        int cacheApproxSize = -1;

        if (cache.getStatus() == Status.STATUS_ALIVE) {
            try {
                cacheApproxSize = cache.getSize();
            } catch (CacheException | IllegalStateException ignored) {
            }
        }

        LoggingEvent loggingEvent = new LoggingEvent(Logger.FQCN, LOGGER, Level.TRACE,
            String.format(MSG_FORMAT, cache.getName(), cache.getStatus(), cacheApproxSize, elem.getObjectKey(), eventType.getId()), null, null);
        StackTraceElement[] stackFrames = new Throwable().getStackTrace();

        for (int a = 1; a < stackFrames.length; a++) {
            if (!STACK_SKIP_CLASS_NAMES.contains(stackFrames[a].getClassName())) {
                stackFrames = ArrayUtils.subarray(stackFrames, a, stackFrames.length);

                break;
            }
        }

        loggingEvent.setCallerData(stackFrames);

        LOGGER.callAppenders(loggingEvent);
    }
}
