package gov.hhs.onc.sdcct.beans.factory.xml.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.SdcctBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class SdcctNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        SpringFactoriesLoader.loadFactories(SdcctBeanDefinitionParser.class, this.getClass().getClassLoader())
            .forEach(beanDefParser -> beanDefParser.getElementBeanClasses().keySet()
                .forEach(beanDefParserElemLocalName -> this.registerBeanDefinitionParser(beanDefParserElemLocalName, beanDefParser)));
    }
}
