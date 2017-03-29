package gov.hhs.onc.sdcct.data.db.cache.impl;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.RegisteredEventListeners;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class CacheFactoryBean extends AbstractCacheComponentFactoryBean<EhCacheCache, CacheConfiguration> {
    private CacheEventListener[] listeners;
    private Strategy persistenceStrategy;
    private Searchable searchable;

    public CacheFactoryBean() {
        super(EhCacheCache.class);
    }

    @Override
    public EhCacheCache getObject() throws Exception {
        String name = this.config.getName();

        if (name == null) {
            this.config.setName((name = this.beanName));
        }

        if (this.searchable != null) {
            this.config.searchable(this.searchable);
        }

        if (this.hasMaxBytesLocalDisk()) {
            this.config.setMaxBytesLocalDisk(this.maxBytesLocalDisk);
        }

        if (this.hasMaxBytesLocalHeap()) {
            this.config.setMaxBytesLocalHeap(this.maxBytesLocalHeap);
        }

        if (this.hasMaxBytesLocalOffHeap()) {
            this.config.setMaxBytesLocalOffHeap(this.maxBytesLocalOffHeap);
        }

        if (this.hasPersistenceStrategy()) {
            this.config.addPersistence(new PersistenceConfiguration().strategy(persistenceStrategy));
        }

        if (this.hasSearchable()) {
            this.config.searchable(this.searchable);
        }

        Cache cache = new Cache(this.config);
        cache.setName(name);

        if (this.hasListeners()) {
            RegisteredEventListeners registeredListeners = cache.getCacheEventNotificationService();

            Stream.of(this.listeners).forEach(registeredListeners::registerListener);
        }

        cache.setCacheManager(this.cacheManager.getCacheManager());

        cache.initialise();

        return new EhCacheCache(cache);
    }

    public EhCacheCacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void setCacheManager(EhCacheCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public boolean hasListeners() {
        return !ArrayUtils.isEmpty(this.listeners);
    }

    @Nullable
    public CacheEventListener[] getListeners() {
        return this.listeners;
    }

    public void setListeners(@Nullable CacheEventListener ... listeners) {
        this.listeners = listeners;
    }

    public boolean hasPersistenceStrategy() {
        return (this.persistenceStrategy != null);
    }

    @Nullable
    public Strategy getPersistenceStrategy() {
        return this.persistenceStrategy;
    }

    public void setPersistenceStrategy(@Nullable Strategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }

    public boolean hasSearchable() {
        return (this.searchable != null);
    }

    @Nullable
    public Searchable getSearchable() {
        return this.searchable;
    }

    public void setSearchable(@Nullable Searchable searchable) {
        this.searchable = searchable;
    }
}
