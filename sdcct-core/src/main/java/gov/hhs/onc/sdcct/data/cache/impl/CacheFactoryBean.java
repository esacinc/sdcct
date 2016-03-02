package gov.hhs.onc.sdcct.data.cache.impl;

import java.util.Optional;
import java.util.stream.Stream;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.RegisteredEventListeners;
import org.apache.commons.lang3.ArrayUtils;

public class CacheFactoryBean extends AbstractCacheComponentFactoryBean<Cache, CacheConfiguration> {
    private CacheEventListener[] listeners;
    private Strategy persistenceStrategy;

    public CacheFactoryBean() {
        super(Cache.class);
    }

    @Override
    public Cache getObject() throws Exception {
        String name = this.config.getName();

        if (name == null) {
            this.config.setName((name = this.beanName));
        }

        Optional.ofNullable(this.maxBytesLocalDisk).ifPresent(this.config::setMaxBytesLocalDisk);
        Optional.ofNullable(this.maxBytesLocalHeap).ifPresent(this.config::setMaxBytesLocalHeap);
        Optional.ofNullable(this.maxBytesLocalOffHeap).ifPresent(this.config::setMaxBytesLocalOffHeap);
        Optional.ofNullable(this.persistenceStrategy).ifPresent(
            persistenceStrategy -> this.config.addPersistence(new PersistenceConfiguration().strategy(persistenceStrategy)));

        Cache cache = new Cache(this.config);
        cache.setName(name);

        if (!ArrayUtils.isEmpty(this.listeners)) {
            RegisteredEventListeners registeredListeners = cache.getCacheEventNotificationService();

            Stream.of(this.listeners).forEach(registeredListeners::registerListener);
        }

        return cache;
    }

    public CacheEventListener[] getListeners() {
        return this.listeners;
    }

    public void setListeners(CacheEventListener ... listeners) {
        this.listeners = listeners;
    }

    public Strategy getPersistenceStrategy() {
        return this.persistenceStrategy;
    }

    public void setPersistenceStrategy(Strategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }
}
