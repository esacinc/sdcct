package gov.hhs.onc.sdcct.beans.factory.impl;

import org.springframework.beans.factory.SmartFactoryBean;

public abstract class AbstractSdcctFactoryBean<T> implements SmartFactoryBean<T> {
    protected Class<T> beanClass;
    protected boolean eagerInit;
    protected boolean prototype;

    protected AbstractSdcctFactoryBean(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public boolean isEagerInit() {
        return this.eagerInit;
    }

    public void setEagerInit(boolean eagerInit) {
        this.eagerInit = eagerInit;
    }

    @Override
    public Class<?> getObjectType() {
        return this.beanClass;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    @Override
    public boolean isSingleton() {
        return !this.isPrototype();
    }
}
