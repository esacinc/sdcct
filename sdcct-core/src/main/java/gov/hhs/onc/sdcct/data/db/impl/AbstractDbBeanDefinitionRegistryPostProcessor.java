package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.beans.factory.impl.AbstractSdcctBeanDefinitionRegistryPostProcessor;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;
import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;

public abstract class AbstractDbBeanDefinitionRegistryPostProcessor<T extends Enum<T> & IdentifiedBean & NamedBean, U, V extends SdcctResource, W extends SdcctRegistry<U, V>, X extends SearchService<U, V, W>>
    extends AbstractSdcctBeanDefinitionRegistryPostProcessor implements BeanClassLoaderAware {
    protected final static String REGISTRY_BEAN_NAME_PREFIX = "registryResource";
    protected final static String SEARCH_SERVICE_BEAN_NAME_PREFIX = "searchServiceResource";

    protected SpecificationType specType;
    protected Class<T> typeClass;
    protected Class<U> beanSuperClass;
    protected Class<? extends U> beanSuperImplClass;
    protected Class<V> entitySuperClass;
    protected Class<W> registryClass;
    protected Class<? extends W> registryImplClass;
    protected Class<X> searchServiceClass;
    protected Class<? extends X> searchServiceImplClass;
    protected ClassLoader beanClassLoader;

    protected AbstractDbBeanDefinitionRegistryPostProcessor(SpecificationType specType, Class<T> typeClass, Class<U> beanSuperClass,
        Class<? extends U> beanSuperImplClass, Class<V> entitySuperClass, Class<W> registryClass, Class<? extends W> registryImplClass,
        Class<X> searchServiceClass, Class<? extends X> searchServiceImplClass) {
        this.specType = specType;
        this.typeClass = typeClass;
        this.beanSuperClass = beanSuperClass;
        this.beanSuperImplClass = beanSuperImplClass;
        this.entitySuperClass = entitySuperClass;
        this.registryClass = registryClass;
        this.registryImplClass = registryImplClass;
        this.searchServiceClass = searchServiceClass;
        this.searchServiceImplClass = searchServiceImplClass;
    }

    @Override
    protected void postProcessBeanDefinitionRegistryInternal(BeanDefinitionRegistry beanDefRegistry) throws Exception {
        Package pkg = this.beanSuperClass.getPackage();
        String specTypeBeanNamePrefix = StringUtils.capitalize(this.specType.getId()), registryBeanNamePrefix =
            REGISTRY_BEAN_NAME_PREFIX + specTypeBeanNamePrefix, searchServiceBeanNamePrefix = SEARCH_SERVICE_BEAN_NAME_PREFIX + specTypeBeanNamePrefix, typeName, registryBeanName;
        Class<? extends U> beanClass, beanImplClass;
        RootBeanDefinition beanDef;

        for (T type : this.typeClass.getEnumConstants()) {
            beanDef =
                ((RootBeanDefinition) BeanDefinitionBuilder.rootBeanDefinition(this.registryImplClass.getName())
                    .addConstructorArgValue((beanClass = SdcctClassUtils.buildInterfaceClass(this.beanClassLoader, this.beanSuperClass, pkg, type.getId())))
                    .addConstructorArgValue((beanImplClass = SdcctClassUtils.buildImplClass(this.beanClassLoader, this.beanSuperImplClass, beanClass)))
                    .getRawBeanDefinition());
            beanDef.setTargetType(ResolvableType.forClassWithGenerics(this.registryClass, beanClass).resolve());
            beanDefRegistry.registerBeanDefinition((registryBeanName = (registryBeanNamePrefix + (typeName = type.getName()))), beanDef);

            beanDef =
                ((RootBeanDefinition) BeanDefinitionBuilder.rootBeanDefinition(this.searchServiceImplClass.getName()).addConstructorArgValue(beanClass)
                    .addConstructorArgValue(beanImplClass).addConstructorArgReference(registryBeanName).getRawBeanDefinition());
            beanDef.setTargetType(ResolvableType.forClassWithGenerics(this.searchServiceClass, beanClass).resolve());
            beanDefRegistry.registerBeanDefinition((searchServiceBeanNamePrefix + typeName), beanDef);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }
}
