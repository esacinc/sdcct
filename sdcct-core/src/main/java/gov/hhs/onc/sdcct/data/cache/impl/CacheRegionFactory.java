package gov.hhs.onc.sdcct.data.cache.impl;

import java.util.Properties;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class CacheRegionFactory extends EhCacheRegionFactory {
    private final static long serialVersionUID = 0L;

    private EhCacheCacheManager cacheManager;

    @Override
    public void start(SessionFactoryOptions settings, Properties props) throws CacheException {
        this.settings = settings;

        this.mbeanRegistrationHelper.registerMBean(this.manager, props);
    }

    public EhCacheCacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void setCacheManager(EhCacheCacheManager cacheManager) {
        this.manager = (this.cacheManager = cacheManager).getCacheManager();
    }
}
