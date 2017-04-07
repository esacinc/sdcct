package gov.hhs.onc.sdcct.data.cache.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.AbstractSdcctFactoryBean;
import javax.annotation.Nullable;
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

    public boolean hasMaxBytesLocalDisk() {
        return (this.maxBytesLocalDisk != null);
    }

    @Nullable
    public String getMaxBytesLocalDisk() {
        return this.maxBytesLocalDisk;
    }

    public void setMaxBytesLocalDisk(@Nullable String maxBytesLocalDisk) {
        this.maxBytesLocalDisk = maxBytesLocalDisk;
    }

    public boolean hasMaxBytesLocalHeap() {
        return (this.maxBytesLocalHeap != null);
    }

    @Nullable
    public String getMaxBytesLocalHeap() {
        return this.maxBytesLocalHeap;
    }

    public void setMaxBytesLocalHeap(@Nullable String maxBytesLocalHeap) {
        this.maxBytesLocalHeap = maxBytesLocalHeap;
    }

    public boolean hasMaxBytesLocalOffHeap() {
        return (this.maxBytesLocalOffHeap != null);
    }

    @Nullable
    public String getMaxBytesLocalOffHeap() {
        return this.maxBytesLocalOffHeap;
    }

    public void setMaxBytesLocalOffHeap(@Nullable String maxBytesLocalOffHeap) {
        this.maxBytesLocalOffHeap = maxBytesLocalOffHeap;
    }
}
