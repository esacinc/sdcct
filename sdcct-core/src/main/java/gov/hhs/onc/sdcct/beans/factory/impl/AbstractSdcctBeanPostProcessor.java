package gov.hhs.onc.sdcct.beans.factory.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class AbstractSdcctBeanPostProcessor<T> implements BeanFactoryAware, BeanPostProcessor {
    protected Class<T> beanClass;
    protected ConfigurableListableBeanFactory beanFactory;

    protected AbstractSdcctBeanPostProcessor(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        try {
            return (this.beanClass.isAssignableFrom(beanClass) ? this.postProcessAfterInitializationInternal(this.beanClass.cast(bean), beanName) : bean);
        } catch (Exception e) {
            throw new FatalBeanException(String.format("Unable to post process bean (name=%s, class=%s) after initialization.", beanName, beanClass.getName()),
                e);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        try {
            return (this.beanClass.isAssignableFrom(beanClass) ? this.postProcessBeforeInitializationInternal(this.beanClass.cast(bean), beanName) : bean);
        } catch (Exception e) {
            throw new FatalBeanException(String.format("Unable to post process bean (name=%s, class=%s) before initialization.", beanName, beanClass.getName()),
                e);
        }
    }

    protected T postProcessAfterInitializationInternal(T bean, String beanName) throws Exception {
        return bean;
    }

    protected T postProcessBeforeInitializationInternal(T bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = ((ConfigurableListableBeanFactory) beanFactory);
    }
}
