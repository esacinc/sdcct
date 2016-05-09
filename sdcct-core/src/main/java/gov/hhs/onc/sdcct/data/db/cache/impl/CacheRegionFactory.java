package gov.hhs.onc.sdcct.data.db.cache.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.DbService;
import gov.hhs.onc.sdcct.data.db.impl.AbstractDbServiceContributor;
import java.util.Map;
import java.util.Properties;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.EhCacheRegionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public class CacheRegionFactory extends EhCacheRegionFactory implements DbService {
    public static class CacheRegionFactoryContributor extends AbstractDbServiceContributor<CacheRegionFactory> {
        public CacheRegionFactoryContributor() {
            super(CacheRegionFactory.class);
        }

        @Override
        public void contribute(StandardServiceRegistryBuilder serviceRegistryBuilder) {
            super.contribute(serviceRegistryBuilder);
            
            
        }

        @Override
        @SuppressWarnings({ CompilerWarnings.RAWTYPES })
        public CacheRegionFactory initiateService(Map configValues, ServiceRegistryImplementor serviceRegistry) {
            CacheRegionFactory service = super.initiateService(configValues, serviceRegistry);

            serviceRegistry.locateServiceBinding(RegionFactory.class).setService(service);

            return service;
        }
    }

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
