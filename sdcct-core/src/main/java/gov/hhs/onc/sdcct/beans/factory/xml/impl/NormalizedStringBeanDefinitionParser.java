package gov.hhs.onc.sdcct.beans.factory.xml.impl;

import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class NormalizedStringBeanDefinitionParser extends AbstractSdcctBeanDefinitionParser {
    private final static String NORMALIZED_STRING_ELEM_LOCAL_NAME = "normalized-string";

    public NormalizedStringBeanDefinitionParser(SdcctNamespaceHandler nsHandler) {
        super(nsHandler, Collections.singletonMap(NORMALIZED_STRING_ELEM_LOCAL_NAME, String.class));
    }

    @Override
    protected AbstractBeanDefinition parseDefinition(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        beanDef.getConstructorArgumentValues().addGenericArgumentValue(StringUtils.normalizeSpace(elem.getTextContent()));

        return beanDef;
    }
}
