package gov.hhs.onc.sdcct.xml.xpath.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.impl.SdcctNamespaceHandler;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.AbstractXmlTransformBeanDefinitionParser;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils;
import gov.hhs.onc.sdcct.xml.xpath.StaticXpathOptions;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class XpathBeanDefinitionParser extends AbstractXmlTransformBeanDefinitionParser {
    private final static String XPATH_ELEM_LOCAL_NAME_PREFIX = "xpath-";
    
    private final static String XPATH_EXEC_ELEM_LOCAL_NAME = XPATH_ELEM_LOCAL_NAME_PREFIX + "executable";
    private final static String XPATH_EXPR_ELEM_LOCAL_NAME = XPATH_ELEM_LOCAL_NAME_PREFIX + "expression";
    private final static String XPATH_STATIC_OPTS_ELEM_LOCAL_NAME = XPATH_ELEM_LOCAL_NAME_PREFIX + STATIC_OPTS_ELEM_LOCAL_NAME_SUFFIX;

    public XpathBeanDefinitionParser(SdcctNamespaceHandler nsHandler) {
        super(nsHandler, Stream.of(new ImmutablePair<>(XPATH_EXEC_ELEM_LOCAL_NAME, SdcctXpathExecutable.class),
            new ImmutablePair<>(XPATH_STATIC_OPTS_ELEM_LOCAL_NAME, StaticXpathOptionsImpl.class)).collect(SdcctStreamUtils.toMap()));
    }

    @Override
    protected AbstractBeanDefinition parseDefinition(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        return (elemLocalName.equals(XPATH_EXEC_ELEM_LOCAL_NAME)
            ? this.parseExecutable(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef)
            : this.parseStaticOptions(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef));
    }

    private AbstractBeanDefinition parseExecutable(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        // noinspection ConstantConditions
        beanDef.setFactoryBeanName(SdcctXpathCompiler.BEAN_NAME);
        beanDef.setFactoryMethodName(SdcctXpathCompiler.COMPILE_METHOD_NAME);

        ConstructorArgumentValues beanDefConstructorArgValues = beanDef.getConstructorArgumentValues();
        // noinspection ConstantConditions
        beanDefConstructorArgValues.addGenericArgumentValue(
            DomUtils.getTextValue(SdcctXmlUtils.findChildElement(elem, elemNsUri, XPATH_EXPR_ELEM_LOCAL_NAME)).trim(), String.class.getName());

        Element staticOptsElem = SdcctXmlUtils.findChildElement(elem, elemNsUri, XPATH_STATIC_OPTS_ELEM_LOCAL_NAME);

        if (staticOptsElem != null) {
            beanDefConstructorArgValues.addGenericArgumentValue(this.parseStaticOptions(parserContext, beanDefRegistry, staticOptsElem, elemNsPrefix, elemNsUri,
                XPATH_STATIC_OPTS_ELEM_LOCAL_NAME, BeanDefinitionBuilder.genericBeanDefinition(StaticXpathOptionsImpl.class).getRawBeanDefinition()),
                StaticXpathOptions.class.getName());
        }

        return beanDef;
    }
}
