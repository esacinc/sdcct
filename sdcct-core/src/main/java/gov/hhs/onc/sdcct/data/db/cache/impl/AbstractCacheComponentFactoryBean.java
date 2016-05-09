package gov.hhs.onc.sdcct.data.db.cache.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.AbstractSdcctFactoryBean;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.cache.ehcache.EhCacheCacheManager;

public abstract class AbstractCacheComponentFactoryBean<T, U> extends AbstractSdcctFactoryBean<T> implements BeanNameAware {
    protected String beanName;
    protected U config;
    protected String maxBytesLocalDisk;
    protected String maxBytesLocalHeap;
    protected String maxBytesLocalOffHeap;
    protected EhCacheCacheManager cacheManager;

    protected AbstractCacheComponentFactoryBean(Class<T> beanClass) {
        super(beanClass);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public U getConfiguration() {
        return this.config;
    }

    public void setConfiguration(U config) {
        this.config = config;
    }

    public String getMaxBytesLocalDisk() {
        return this.maxBytesLocalDisk;
    }

    public void setMaxBytesLocalDisk(String maxBytesLocalDisk) {
        this.maxBytesLocalDisk = maxBytesLocalDisk;
    }

    public String getMaxBytesLocalHeap() {
        return this.maxBytesLocalHeap;
    }

    public void setMaxBytesLocalHeap(String maxBytesLocalHeap) {
        this.maxBytesLocalHeap = maxBytesLocalHeap;
    }

    public String getMaxBytesLocalOffHeap() {
        return this.maxBytesLocalOffHeap;
    }

    public void setMaxBytesLocalOffHeap(String maxBytesLocalOffHeap) {
        this.maxBytesLocalOffHeap = maxBytesLocalOffHeap;
    }
}
