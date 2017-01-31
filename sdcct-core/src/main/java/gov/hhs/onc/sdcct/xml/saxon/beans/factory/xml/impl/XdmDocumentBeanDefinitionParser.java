package gov.hhs.onc.sdcct.xml.saxon.beans.factory.xml.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.impl.AbstractSdcctBeanDefinitionParser;
import gov.hhs.onc.sdcct.beans.factory.xml.impl.SdcctNamespaceHandler;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import java.util.Collections;
import javax.xml.transform.Source;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class XdmDocumentBeanDefinitionParser extends AbstractSdcctBeanDefinitionParser {
    public final static String DOC_ELEM_LOCAL_NAME = "document";

    private final static String DOC_URI_NODE_NAME = DOC_ELEM_LOCAL_NAME + "-uri";
    private final static String PUBLIC_ID_ATTR_NAME = "public-" + ID_ATTRIBUTE;
    private final static String SYS_ID_ATTR_NAME = "system-" + ID_ATTRIBUTE;

    private final static String DOC_URI_PROP_NAME = "documentUri";
    private final static String PUBLIC_ID_PROP_NAME = "publicId";
    private final static String SYS_ID_PROP_NAME = "systemId";

    public XdmDocumentBeanDefinitionParser(SdcctNamespaceHandler nsHandler) {
        super(nsHandler, Collections.singletonMap(DOC_ELEM_LOCAL_NAME, XdmDocument.class));
    }

    @Override
    protected AbstractBeanDefinition parseDefinition(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        beanDef.setFactoryBeanName(SdcctDocumentBuilder.BEAN_NAME);
        beanDef.setFactoryMethodName(SdcctDocumentBuilder.BUILD_METHOD_NAME);
        beanDef.getConstructorArgumentValues().addGenericArgumentValue(DomUtils.getTextValue(elem).trim(), Source.class.getName());

        MutablePropertyValues beanDefPropValues = beanDef.getPropertyValues();

        if (elem.hasAttributeNS(elemNsUri, DOC_URI_NODE_NAME)) {
            beanDefPropValues.addPropertyValue(DOC_URI_PROP_NAME, elem.getAttributeNS(elemNsUri, DOC_URI_NODE_NAME));
        }

        if (elem.hasAttributeNS(elemNsUri, PUBLIC_ID_ATTR_NAME)) {
            beanDefPropValues.addPropertyValue(PUBLIC_ID_PROP_NAME, elem.getAttributeNS(elemNsUri, PUBLIC_ID_ATTR_NAME));
        }

        if (elem.hasAttributeNS(elemNsUri, SYS_ID_ATTR_NAME)) {
            beanDefPropValues.addPropertyValue(SYS_ID_PROP_NAME, elem.getAttributeNS(elemNsUri, SYS_ID_ATTR_NAME));
        }

        return beanDef;
    }
}
