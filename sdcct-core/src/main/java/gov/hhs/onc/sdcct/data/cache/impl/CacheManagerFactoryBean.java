package gov.hhs.onc.sdcct.data.cache.impl;

import java.util.Optional;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.SizeOfPolicyConfiguration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class CacheManagerFactoryBean extends AbstractCacheComponentFactoryBean<CacheManager, Configuration> implements DisposableBean {
    private Cache[] caches;
    private EhCacheCache[] wrappedCaches;
    private CacheManager cacheManager;
    private EhCacheCacheManager wrappedCacheManager;

    public CacheManagerFactoryBean() {
        super(CacheManager.class);
    }

    @Override
    public void destroy() throws Exception {
        this.cacheManager.shutdown();
    }

    @Override
    public CacheManager getObject() throws Exception {
        if (this.config.getName() == null) {
            this.config.setName(this.beanName);
        }

        this.config.addSizeOfPolicy(new SizeOfPolicyConfiguration().maxDepth(Integer.MAX_VALUE));

        Optional.ofNullable(this.maxBytesLocalDisk).ifPresent(this.config::setMaxBytesLocalDisk);
        Optional.ofNullable(this.maxBytesLocalHeap).ifPresent(this.config::setMaxBytesLocalHeap);
        Optional.ofNullable(this.maxBytesLocalOffHeap).ifPresent(this.config::setMaxBytesLocalOffHeap);

        this.cacheManager = new CacheManager(this.config);
        this.wrappedCaches = new EhCacheCache[this.caches.length];

        Cache cache;

        for (int a = 0; a < this.caches.length; a++) {
            (cache = this.caches[a]).setCacheManager(this.cacheManager);

            cache.initialise();

            this.wrappedCaches[a] = new EhCacheCache(cache);
        }

        (this.wrappedCacheManager = new EhCacheCacheManager(this.cacheManager)).initializeCaches();

        return this.cacheManager;
    }

    public Cache[] getCaches() {
        return this.caches;
    }

    public void setCaches(Cache ... caches) {
        this.caches = caches;
    }

    public EhCacheCacheManager getWrappedCacheManager() {
        return this.wrappedCacheManager;
    }

    public EhCacheCache[] getWrappedCaches() {
        return this.wrappedCaches;
    }
}
