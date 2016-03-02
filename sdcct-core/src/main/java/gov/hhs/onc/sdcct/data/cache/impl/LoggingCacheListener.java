package gov.hhs.onc.sdcct.data.cache.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListenerAdapter;
import org.apache.commons.lang3.text.StrBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("cacheListenerLogging")
public class LoggingCacheListener extends CacheEventListenerAdapter {
    private static enum CacheEventType implements IdentifiedBean {
        EVICTED, EXPIRED, PUT;

        private final String id;

        private CacheEventType() {
            this.id = this.name().toLowerCase();
        }

        @Override
        public String getId() {
            return this.id;
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingCacheListener.class);

    @Override
    public void notifyElementEvicted(Ehcache cache, Element elem) {
        LOGGER.trace(buildMessage(cache, elem, CacheEventType.EVICTED));
    }

    @Override
    public void notifyElementExpired(Ehcache cache, Element elem) {
        LOGGER.trace(buildMessage(cache, elem, CacheEventType.EXPIRED));
    }

    @Override
    public void notifyElementPut(Ehcache cache, Element elem) throws CacheException {
        LOGGER.trace(buildMessage(cache, elem, CacheEventType.PUT));
    }

    private static String buildMessage(Ehcache cache, Element elem, CacheEventType eventType) {
        StrBuilder msgBuilder = new StrBuilder("Cache (name=");
        msgBuilder.append(cache.getName());
        msgBuilder.append(", size=");
        msgBuilder.append(cache.getSize());
        msgBuilder.append(") element (key=");
        msgBuilder.append(elem.getObjectKey());
        msgBuilder.append(") ");
        msgBuilder.append(eventType.getId());
        msgBuilder.append(".");

        return msgBuilder.build();
    }
}
