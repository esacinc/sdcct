package gov.hhs.onc.sdcct.beans.factory.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

public abstract class AbstractSdcctBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    protected ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefRegistry) throws BeansException {
        try {
            this.postProcessBeanDefinitionRegistryInternal(beanDefRegistry);
        } catch (Exception e) {
            throw new FatalBeanException("Unable to post process bean definition registry.", e);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;

        try {
            this.postProcessBeanFactoryInternal(beanFactory);
        } catch (Exception e) {
            throw new FatalBeanException("Unable to post process bean factory.", e);
        }
    }

    protected void postProcessBeanDefinitionRegistryInternal(BeanDefinitionRegistry beanDefRegistry) throws Exception {
    }

    protected void postProcessBeanFactoryInternal(ConfigurableListableBeanFactory beanFactory) throws Exception {
    }
}
