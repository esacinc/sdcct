package gov.hhs.onc.sdcct.build.xml.jaxb.utils

import gov.hhs.onc.sdcct.net.SdcctUris
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils
import groovy.xml.DOMBuilder
import javax.annotation.Nullable
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import org.apache.commons.collections4.IteratorUtils
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList

final class SdcctFhirCodegenUtils {
    final static XPath XPATH = SdcctXpathCodegenUtils.buildXpath(SdcctXmlPrefixes.FHIR_XPATH, SdcctUris.FHIR_URL_VALUE)
    
    final static String RESOURCE_ITEM_ELEM_XPATH_EXPR_PREFIX =
        "/${SdcctXmlPrefixes.FHIR_XPATH}:Bundle/${SdcctXmlPrefixes.FHIR_XPATH}:entry/${SdcctXmlPrefixes.FHIR_XPATH}:resource/${SdcctXmlPrefixes.FHIR_XPATH}:"
    
    final static String RESOURCE_ITEM_ELEM_XPATH_EXPR_SUFFIX = "[${SdcctXmlPrefixes.FHIR_XPATH}:status[@value != 'retired']]"
    
    final static String URL_NODE_NAME = "url"
    final static String VALUE_NODE_NAME = "value"
    
    final static String BASE_TYPE_ELEM_NAME = "baseType"
    final static String CODE_ELEM_NAME = "code"
    final static String CODE_SYSTEM_ELEM_NAME = "CodeSystem"
    final static String COMPOSE_ELEM_NAME = "compose"
    final static String CONCEPT_ELEM_NAME = "concept"
    final static String DEFINITION_ELEM_NAME = "definition"
    final static String DISPLAY_ELEM_NAME = "display"
    final static String EXCLUDE_ELEM_NAME = "exclude"
    final static String EXTENSION_ELEM_NAME = "extension"
    final static String FHIR_VERSION_ELEM_NAME = "fhirVersion"
    final static String ID_ELEM_NAME = "id"
    final static String INCLUDE_ELEM_NAME = "include"
    final static String KIND_ELEM_NAME = "kind"
    final static String NAME_ELEM_NAME = "name"
    final static String SNAPSHOT_ELEM_NAME = "snapshot"
    final static String STRUCTURE_DEFINITION_ELEM_NAME = "StructureDefinition"
    final static String SYSTEM_ELEM_NAME = "system"
    final static String VALUE_SET_ELEM_NAME = "ValueSet"
    final static String VALUE_STRING_ELEM_NAME = VALUE_NODE_NAME + "String"
    final static String VALUE_URI_ELEM_NAME = VALUE_NODE_NAME + "Uri"
    final static String VERSION_ELEM_NAME = "version"
    
    final static String CLASS_ATTR_NAME = "class"
    final static String URI_ATTR_NAME = "uri"
    
    final static String CODE_MEMBER_NAME_PREFIX = "code"
    final static String TYPE_MEMBER_NAME_PREFIX = "type"
    final static String VALUE_SET_MEMBER_NAME_PREFIX = "valueSet"
    
    final static String ID_MEMBER_NAME_SUFFIX = "Id"
    final static String NAME_MEMBER_NAME_SUFFIX = "Name"
    final static String OID_MEMBER_NAME_SUFFIX = "Oid"
    final static String SYSTEM_MEMBER_NAME_SUFFIX = "System"
    final static String URI_MEMBER_NAME_SUFFIX = "Uri"
    final static String VERSION_MEMBER_NAME_SUFFIX = "Version"
    
    final static String CODE_SYSTEM_ID_MEMBER_NAME = CODE_MEMBER_NAME_PREFIX + SYSTEM_MEMBER_NAME_SUFFIX + ID_MEMBER_NAME_SUFFIX
    final static String CODE_SYSTEM_NAME_MEMBER_NAME = CODE_MEMBER_NAME_PREFIX + SYSTEM_MEMBER_NAME_SUFFIX + NAME_MEMBER_NAME_SUFFIX
    final static String CODE_SYSTEM_OID_MEMBER_NAME = CODE_MEMBER_NAME_PREFIX + SYSTEM_MEMBER_NAME_SUFFIX + OID_MEMBER_NAME_SUFFIX
    final static String CODE_SYSTEM_URI_MEMBER_NAME = CODE_MEMBER_NAME_PREFIX + SYSTEM_MEMBER_NAME_SUFFIX + URI_MEMBER_NAME_SUFFIX
    final static String CODE_SYSTEM_VERSION_MEMBER_NAME = CODE_MEMBER_NAME_PREFIX + SYSTEM_MEMBER_NAME_SUFFIX + VERSION_MEMBER_NAME_SUFFIX
    final static String TYPE_ID_MEMBER_NAME = TYPE_MEMBER_NAME_PREFIX + ID_MEMBER_NAME_SUFFIX
    final static String TYPE_NAME_MEMBER_NAME = TYPE_MEMBER_NAME_PREFIX + NAME_MEMBER_NAME_SUFFIX
    final static String TYPE_PATTERN_MEMBER_NAME = TYPE_MEMBER_NAME_PREFIX + "Pattern"
    final static String TYPE_URI_MEMBER_NAME = TYPE_MEMBER_NAME_PREFIX + URI_MEMBER_NAME_SUFFIX
    final static String TYPE_VERSION_MEMBER_NAME = TYPE_MEMBER_NAME_PREFIX + VERSION_MEMBER_NAME_SUFFIX
    final static String VALUE_SET_ID_MEMBER_NAME = VALUE_SET_MEMBER_NAME_PREFIX + ID_MEMBER_NAME_SUFFIX
    final static String VALUE_SET_NAME_MEMBER_NAME = VALUE_SET_MEMBER_NAME_PREFIX + NAME_MEMBER_NAME_SUFFIX
    final static String VALUE_SET_OID_MEMBER_NAME = VALUE_SET_MEMBER_NAME_PREFIX + OID_MEMBER_NAME_SUFFIX
    final static String VALUE_SET_URI_MEMBER_NAME = VALUE_SET_MEMBER_NAME_PREFIX + URI_MEMBER_NAME_SUFFIX
    final static String VALUE_SET_VERSION_MEMBER_NAME = VALUE_SET_MEMBER_NAME_PREFIX + VERSION_MEMBER_NAME_SUFFIX
    
    private SdcctFhirCodegenUtils() {
    }
    
    @Nullable
    static String buildExtensionValue(@Nullable Node node, String url, String valueElemName, boolean child) {
        return SdcctXmlUtils.findChildElement((child ? SdcctXmlUtils.findChildElements(node, SdcctUris.FHIR_URL_VALUE, EXTENSION_ELEM_NAME) :
            SdcctXmlUtils.findDescendantElements(node, SdcctUris.FHIR_URL_VALUE, EXTENSION_ELEM_NAME))
            .find{ Objects.equals(it.getAttribute(URL_NODE_NAME), url) }, SdcctUris.FHIR_URL_VALUE, valueElemName)?.getAttribute(VALUE_NODE_NAME)
    }
    
    @Nullable
    static String buildBaseType(@Nullable Node node) {
        return SdcctXmlUtils.findChildElement(node, SdcctUris.FHIR_URL_VALUE, BASE_TYPE_ELEM_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    
    @Nullable
    static String buildFhirVersion(@Nullable Node node) {
        return SdcctXmlUtils.findChildElement(node, SdcctUris.FHIR_URL_VALUE, FHIR_VERSION_ELEM_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    
    @Nullable
    static String buildVersion(@Nullable Node node) {
        return SdcctXmlUtils.findChildElement(node, SdcctUris.FHIR_URL_VALUE, VERSION_ELEM_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    
    static String buildUri(Element elem) {
        return SdcctXmlUtils.findChildElement(elem, SdcctUris.FHIR_URL_VALUE, URL_NODE_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    @Nullable
    static String buildName(Element elem) {
        return SdcctXmlUtils.findChildElement(elem, SdcctUris.FHIR_URL_VALUE, NAME_ELEM_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    
    @Nullable
    static String buildId(@Nullable Node node) {
        return SdcctXmlUtils.findChildElement(node, SdcctUris.FHIR_URL_VALUE, ID_ELEM_NAME)?.getAttribute(VALUE_NODE_NAME)
    }
    
    static List<Element> buildResourceItemElements(String resourceItemElemName, List<File> dataFiles) {
        List<Element> resourceElems = new ArrayList<>()
        FileReader dataFileReader = null
        
        for (File dataFile : dataFiles) {
            try {
                IteratorUtils.nodeListIterator(((NodeList) XPATH.evaluate((RESOURCE_ITEM_ELEM_XPATH_EXPR_PREFIX + resourceItemElemName +
                    RESOURCE_ITEM_ELEM_XPATH_EXPR_SUFFIX), DOMBuilder.parse((dataFileReader = new FileReader(dataFile))), XPathConstants.NODESET)))
                    .each{ resourceElems.add(((Element) it)) }
            } finally {
                if (dataFileReader != null) {
                    dataFileReader.close()
                    dataFileReader = null
                }
            }
        }
        
        return resourceElems
    }
}
