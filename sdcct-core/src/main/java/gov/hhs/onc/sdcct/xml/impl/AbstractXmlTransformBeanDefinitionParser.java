package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.beans.factory.xml.impl.AbstractSdcctBeanDefinitionParser;
import gov.hhs.onc.sdcct.beans.factory.xml.impl.SdcctNamespaceHandler;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils;
import java.util.List;
import java.util.Map;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XdmValue;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public abstract class AbstractXmlTransformBeanDefinitionParser extends AbstractSdcctBeanDefinitionParser {
    protected final static String STATIC_ELEM_LOCAL_NAME_PREFIX = "static-";

    protected final static String STATIC_OPTS_ELEM_LOCAL_NAME_SUFFIX = STATIC_ELEM_LOCAL_NAME_PREFIX + "options";

    protected final static String STATIC_NS_ELEM_LOCAL_NAME = STATIC_ELEM_LOCAL_NAME_PREFIX + "namespace";
    protected final static String STATIC_POOLED_DOC_ELEM_LOCAL_NAME =
        STATIC_ELEM_LOCAL_NAME_PREFIX + "pooled-" + XdmDocumentBeanDefinitionParser.DOC_ELEM_LOCAL_NAME;
    protected final static String STATIC_VAR_ELEM_LOCAL_NAME = STATIC_ELEM_LOCAL_NAME_PREFIX + "variable";

    protected final static String PREFIX_ATTR_NAME = "prefix";

    protected final static String NAMESPACES_PROP_NAME = "namespaces";
    protected final static String POOLED_DOCS_PROP_NAME = "pooledDocuments";
    protected final static String VARS_PROP_NAME = "variables";

    protected AbstractXmlTransformBeanDefinitionParser(SdcctNamespaceHandler nsHandler, Map<String, Class<?>> elemBeanClasses) {
        super(nsHandler, elemBeanClasses);
    }

    protected AbstractBeanDefinition parseStaticOptions(ParserContext parserContext, BeanDefinitionRegistry beanDefRegistry, Element elem, String elemNsPrefix,
        String elemNsUri, String elemLocalName, AbstractBeanDefinition beanDef) throws Exception {
        super.parseDefinition(parserContext, beanDefRegistry, elem, elemNsPrefix, elemNsUri, elemLocalName, beanDef);

        MutablePropertyValues staticOptsPropValues = beanDef.getPropertyValues();
        List<Element> staticNsElems = SdcctXmlUtils.findChildElements(elem, elemNsUri, STATIC_NS_ELEM_LOCAL_NAME);

        if (!staticNsElems.isEmpty()) {
            ManagedMap<String, String> staticNamespaces = new ManagedMap<>(staticNsElems.size());
            staticNamespaces.setMergeEnabled(true);

            for (Element staticNsElem : staticNsElems) {
                staticNamespaces.put(staticNsElem.getAttribute(PREFIX_ATTR_NAME), DomUtils.getTextValue(staticNsElem).trim());
            }

            staticOptsPropValues.addPropertyValue(NAMESPACES_PROP_NAME, staticNamespaces);
        }

        List<Element> staticVarElems = SdcctXmlUtils.findChildElements(elem, elemNsUri, STATIC_VAR_ELEM_LOCAL_NAME);

        if (!staticVarElems.isEmpty()) {
            ManagedMap<QName, Object> staticVars = new ManagedMap<>(staticVarElems.size());
            staticVars.setMergeEnabled(true);
            staticVars.setValueTypeName(XdmValue.class.getName());

            for (Element staticVarElem : staticVarElems) {
                staticVars.put(new QName(staticVarElem.getAttribute(NAME_ATTRIBUTE)), DomUtils.getTextValue(staticVarElem).trim());
            }

            staticOptsPropValues.addPropertyValue(VARS_PROP_NAME, staticVars);
        }

        List<Element> staticPooledDocElems = SdcctXmlUtils.findChildElements(elem, elemNsUri, STATIC_POOLED_DOC_ELEM_LOCAL_NAME);

        if (!staticPooledDocElems.isEmpty()) {
            XdmDocumentBeanDefinitionParser docBeanDefParser =
                ((XdmDocumentBeanDefinitionParser) this.nsHandler.getBeanDefinitionParsers().get(XdmDocumentBeanDefinitionParser.DOC_ELEM_LOCAL_NAME));

            ManagedList<Object> staticPooledDocs = new ManagedList<>(staticPooledDocElems.size());
            staticPooledDocs.setMergeEnabled(true);
            staticPooledDocs.setElementTypeName(XdmDocument.class.getName());

            for (Element staticPooledDocElem : staticPooledDocElems) {
                staticPooledDocs.add(
                    docBeanDefParser.parse(staticPooledDocElem, new ParserContext(parserContext.getReaderContext(), parserContext.getDelegate(), beanDef)));
            }

            staticOptsPropValues.addPropertyValue(POOLED_DOCS_PROP_NAME, staticPooledDocs);
        }

        return beanDef;
    }
}
