package gov.hhs.onc.sdcct.data.cache.impl;

import java.util.Properties;
import net.sf.ehcache.CacheManager;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;

public class CacheRegionFactory extends EhCacheRegionFactory {
    private final static long serialVersionUID = 0L;

    @Override
    public void start(SessionFactoryOptions settings, Properties props) throws CacheException {
        this.settings = settings;

        this.mbeanRegistrationHelper.registerMBean(this.manager, props);
    }

    public CacheManager getCacheManager() {
        return this.manager;
    }

    public void setCacheManager(CacheManager manager) {
        this.manager = manager;
    }
}
