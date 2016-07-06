package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.impl.SdcctNamespaceHandler;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.impl.AbstractXmlTransformBeanDefinitionParser;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XdmDocumentBeanDefinitionParser;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils;
import gov.hhs.onc.sdcct.xml.xslt.StaticXsltOptions;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class XsltBeanDefinitionParser extends AbstractXmlTransformBeanDefinitionParser {
    private final static String XSLT_ELEM_LOCAL_NAME_PREFIX = "xslt-";

    private final static String XSLT_EXEC_ELEM_LOCAL_NAME = XSLT_ELEM_LOCAL_NAME_PREFIX + "executable";
    private final static String XSLT_DOC_ELEM_LOCAL_NAME = XSLT_ELEM_LOCAL_NAME_PREFIX + XdmDocumentBeanDefinitionParser.DOC_ELEM_LOCAL_NAME;
    private final static String XSLT_STATIC_OPTS_ELEM_LOCAL_NAME = XSLT_ELEM_LOCAL_NAME_PREFIX + STATIC_OPTS_ELEM_LOCAL_NAME_SUFFIX;

    public XsltBeanDefinitionParser(SdcctNamespaceHandler nsHandler) {
        super(nsHandler, Stream.of(new ImmutablePair<>(XSLT_EXEC_ELEM_LOCAL_NAME, SdcctXsltExecutable.class),
            new ImmutablePair<>(XSLT_STATIC_OPTS_ELEM_LOCAL_NAME, StaticXsltOptionsImpl.class)).collect(SdcctStreamUtils.toMap()));
    }

    @Override
    protected AbstractBeanDefinition parseDefinition(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        return (elemLocalName.equals(XSLT_EXEC_ELEM_LOCAL_NAME)
            ? this.parseExecutable(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef)
            : this.parseStaticOptions(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef));
    }

    private AbstractBeanDefinition parseExecutable(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        // noinspection ConstantConditions
        beanDef.setFactoryBeanName(SdcctXsltCompiler.BEAN_NAME);
        beanDef.setFactoryMethodName(SdcctXsltCompiler.COMPILE_METHOD_NAME);

        ConstructorArgumentValues beanDefConstructorArgValues = beanDef.getConstructorArgumentValues();
        // noinspection ConstantConditions
        beanDefConstructorArgValues.addGenericArgumentValue(this.nsHandler.getBeanDefinitionParsers().get(XdmDocumentBeanDefinitionParser.DOC_ELEM_LOCAL_NAME)
            .parse(SdcctXmlUtils.findChildElement(elem, elemNsUri, XSLT_DOC_ELEM_LOCAL_NAME),
                new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), beanDef)),
            XdmDocument.class.getName());

        Element staticOptsElem = SdcctXmlUtils.findChildElement(elem, elemNsUri, XSLT_STATIC_OPTS_ELEM_LOCAL_NAME);

        if (staticOptsElem != null) {
            beanDefConstructorArgValues.addGenericArgumentValue(this.parseStaticOptions(parserContext, beanDefRegistry, staticOptsElem, elemNsPrefix, elemNsUri,
                XSLT_STATIC_OPTS_ELEM_LOCAL_NAME, BeanDefinitionBuilder.genericBeanDefinition(StaticXsltOptionsImpl.class).getRawBeanDefinition()),
                StaticXsltOptions.class.getName());
        }

        return beanDef;
    }
}
