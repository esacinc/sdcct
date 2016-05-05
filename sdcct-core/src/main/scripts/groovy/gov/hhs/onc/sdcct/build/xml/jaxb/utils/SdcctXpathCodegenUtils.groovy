package gov.hhs.onc.sdcct.build.xml.jaxb.utils

import java.util.Map.Entry
import javax.annotation.Nullable
import javax.xml.XMLConstants
import javax.xml.namespace.NamespaceContext
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathFactory
import net.sf.saxon.om.NamespaceResolver
import net.sf.saxon.xpath.XPathFactoryImpl
import org.apache.commons.collections4.IteratorUtils
import org.apache.commons.collections4.bidimap.DualLinkedHashBidiMap
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.tuple.ImmutablePair

final class SdcctXpathCodegenUtils {
    static class CodegenNamespaceContext extends DualLinkedHashBidiMap<String, String> implements NamespaceContext, NamespaceResolver {
        CodegenNamespaceContext(Entry<String, String> ... nsEntries) {
            super()
            
            this[XMLConstants.XML_NS_PREFIX] = XMLConstants.XML_NS_URI
            this[XMLConstants.XMLNS_ATTRIBUTE] = XMLConstants.XMLNS_ATTRIBUTE_NS_URI
            
            nsEntries.each{ this[it.key] = it.value }
        }

        @Override
        Iterator<String> iteratePrefixes() {
            return this.keySet().iterator()
        }
        
        @Override
        Iterator<String> getPrefixes(String nsUri) {
            return (this.containsValue(nsUri) ? IteratorUtils.singletonIterator(this.getPrefix(nsUri)) : IteratorUtils.emptyIterator())
        }
        
        @Nullable
        @Override
        String getPrefix(String nsUri) {
            return this.getKey(nsUri)
        }
        
        @Nullable
        @Override
        String getURIForPrefix(String nsPrefix, boolean useDefault) {
            return (nsPrefix.isEmpty() ? (useDefault ? this.get(nsPrefix, StringUtils.EMPTY) : StringUtils.EMPTY) : this.getNamespaceURI(nsPrefix))
        }
        
        @Nullable
        @Override
        String getNamespaceURI(String nsPrefix) {
            return this[nsPrefix]
        }
    }
    
    final static XPathFactory XPATH_FACTORY = new XPathFactoryImpl()
    
    private SdcctXpathCodegenUtils() {
    }
    
    static XPath buildXpath(String nsPrefix, String nsUri) {
        XPath xpath = XPATH_FACTORY.newXPath()
        xpath.setNamespaceContext(new CodegenNamespaceContext(new ImmutablePair<>(nsPrefix, nsUri)))
        
        return xpath
    }
}
