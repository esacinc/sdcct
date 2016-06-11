package gov.hhs.onc.sdcct.beans.factory.xml.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.SdcctBeanDefinitionParser;
import gov.hhs.onc.sdcct.config.utils.SdcctOptionUtils;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Nullable;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public abstract class AbstractSdcctBeanDefinitionParser extends AbstractBeanDefinitionParser implements SdcctBeanDefinitionParser {
    protected Map<String, Class<?>> elemBeanClasses = new TreeMap<>();

    protected AbstractSdcctBeanDefinitionParser(Map<String, Class<?>> elemBeanClasses) {
        this.elemBeanClasses.putAll(elemBeanClasses);
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element elem, ParserContext parserContext) {
        String elemNsPrefix = elem.getPrefix(), elemNsUri = elem.getNamespaceURI(), elemLocalName = elem.getLocalName();
        Class<?> beanClass = this.getBeanClass(elem);
        AbstractBeanDefinition beanDef = BeanDefinitionBuilder.genericBeanDefinition(beanClass).getRawBeanDefinition();

        try {
            this.parseDefinition(parserContext, parserContext.getRegistry(), elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);
        } catch (Exception e) {
            throw new FatalBeanException(String.format("Unable to parse bean definition (class=%s) XML element (nsPrefix=%s, nsUri=%s, localName=%s).",
                beanClass, elemNsPrefix, elemNsUri, elemLocalName), e);
        }

        return beanDef;
    }

    protected AbstractBeanDefinition parseDefinition(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        if (beanDef.hasAttribute(BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE) && SdcctOptionUtils
            .getBooleanValue(BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE, beanDef.getAttribute(BeanDefinitionParserDelegate.ABSTRACT_ATTRIBUTE))) {
            beanDef.setAbstract(true);
        }

        if (elem.hasAttribute(BeanDefinitionParserDelegate.PARENT_ATTRIBUTE)) {
            beanDef.setParentName(elem.getAttribute(BeanDefinitionParserDelegate.PARENT_ATTRIBUTE));
        }

        return beanDef;
    }

    @Nullable
    @Override
    public String getBeanClassName(Element elem) {
        // noinspection ConstantConditions
        return (this.hasBeanClass(elem) ? this.getBeanClass(elem).getName() : null);
    }

    @Override
    public boolean hasBeanClass(Element elem) {
        return this.elemBeanClasses.containsKey(elem.getLocalName());
    }

    @Nullable
    @Override
    public Class<?> getBeanClass(Element elem) {
        return this.elemBeanClasses.get(elem.getLocalName());
    }

    @Override
    public Map<String, Class<?>> getElementBeanClasses() {
        return this.elemBeanClasses;
    }
}
