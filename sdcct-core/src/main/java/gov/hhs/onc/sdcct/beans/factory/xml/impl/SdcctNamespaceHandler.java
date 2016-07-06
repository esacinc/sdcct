package gov.hhs.onc.sdcct.beans.factory.xml.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.SdcctBeanDefinitionParser;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class SdcctNamespaceHandler extends NamespaceHandlerSupport {
    private Map<String, SdcctBeanDefinitionParser> beanDefParsers = new TreeMap<>();

    @Override
    public void init() {
        SdcctBeanDefinitionParser beanDefParser;

        for (Class<?> beanDefClass : ClassUtils
            .convertClassNamesToClasses(SpringFactoriesLoader.loadFactoryNames(SdcctBeanDefinitionParser.class, this.getClass().getClassLoader()))) {
            try {
                beanDefParser = ((SdcctBeanDefinitionParser) ConstructorUtils.invokeConstructor(beanDefClass, this));
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                throw new ApplicationContextException(String.format("Unable to instantiate bean definition parser (class=%s).", beanDefClass.getName()), e);
            }

            for (String beanDefParserElemLocalName : beanDefParser.getElementBeanClasses().keySet()) {
                this.beanDefParsers.put(beanDefParserElemLocalName, beanDefParser);

                this.registerBeanDefinitionParser(beanDefParserElemLocalName, beanDefParser);
            }
        }
    }

    public Map<String, SdcctBeanDefinitionParser> getBeanDefinitionParsers() {
        return this.beanDefParsers;
    }
}
