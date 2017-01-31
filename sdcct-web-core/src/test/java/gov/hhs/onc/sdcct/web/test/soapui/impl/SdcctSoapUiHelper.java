package gov.hhs.onc.sdcct.web.test.soapui.impl;

import com.eviware.soapui.model.propertyexpansion.PropertyExpansionContext;
import com.eviware.soapui.support.GroovyUtils;
import com.eviware.soapui.support.XmlHolder;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.XMLConstants;
import javax.xml.soap.SOAPConstants;
import net.sf.saxon.lib.NamespaceConstant;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.cxf.binding.soap.Soap12;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.Names;
import org.testng.Assert;

public class SdcctSoapUiHelper {
    public final static SdcctSoapUiHelper INSTANCE = new SdcctSoapUiHelper();

    private final static Map<String, String> DEFAULT_XML_NAMESPACES =
        Stream
            .of(new ImmutablePair<>(SdcctXmlPrefixes.XSI, XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI),
                new ImmutablePair<>(SdcctXmlPrefixes.SOAP, SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE),
                new ImmutablePair<>(JAXWSAConstants.WSA_PREFIX, Names.WSA_NAMESPACE_NAME), new ImmutablePair<>(SdcctXmlPrefixes.FHIR, SdcctUris.FHIR_URL_VALUE),
                new ImmutablePair<>(SdcctXmlPrefixes.XHTML, NamespaceConstant.XHTML),
                new ImmutablePair<>(SdcctXmlPrefixes.IHE, SdcctUris.IHE_ITI_RFD_URN_VALUE), new ImmutablePair<>(SdcctXmlPrefixes.SDC, SdcctUris.IHE_QRPH_SDC_URN_VALUE))
            .collect(SdcctStreamUtils.toMap());

    private final static String SOAP_FAULT_COUNT_XPATH_EXPR =
        ("count(//" + SdcctXmlPrefixes.SOAP + SdcctStringUtils.COLON + Soap12.getInstance().getFault().getLocalPart() + SdcctStringUtils.R_PAREN);

    public void assertSoapFault(PropertyExpansionContext propExpansionContext, String xml, boolean state) throws Exception {
        this.assertNodeValuesMatch(propExpansionContext, xml, SOAP_FAULT_COUNT_XPATH_EXPR, Integer.toString((state ? 1 : 0)));
    }

    public void assertNodeValuesMatch(PropertyExpansionContext propExpansionContext, String xml, String xpathExpr, @Nullable Object expectedNodeValues)
        throws Exception {
        this.assertNodeValuesMatch(propExpansionContext, xml, null, xpathExpr, expectedNodeValues);
    }

    public void assertNodeValuesMatch(PropertyExpansionContext propExpansionContext, String xml, @Nullable Map<String, String> xmlNamespaces, String xpathExpr,
        @Nullable Object expectedNodeValues) throws Exception {
        this.assertNodeValuesMatch(this.buildNodeValues(propExpansionContext, xml, xmlNamespaces, xpathExpr), expectedNodeValues);
    }

    public void assertNodeValuesMatch(@Nullable Object nodeValues, @Nullable Object expectedNodeValues) throws Exception {
        Assert.assertTrue(Objects.deepEquals(nodeValues, expectedNodeValues),
            String.format("Node values do not match: expectedNodeValues=[%s], nodeValues=[%s]", expectedNodeValues, nodeValues));
    }

    @Nullable
    public Object buildNodeValues(PropertyExpansionContext propExpansionContext, String xml, String xpathExpr) throws Exception {
        return this.buildNodeValues(propExpansionContext, xml, null, xpathExpr);
    }

    @Nullable
    public Object buildNodeValues(PropertyExpansionContext propExpansionContext, String xml, @Nullable Map<String, String> xmlNamespaces, String xpathExpr)
        throws Exception {
        return this.buildXmlHolder(this.buildGroovyUtils(propExpansionContext), xml, xmlNamespaces).get(xpathExpr);
    }

    public XmlHolder buildXmlHolder(GroovyUtils groovyUtils, String xml) throws Exception {
        return this.buildXmlHolder(groovyUtils, xml, null);
    }

    public XmlHolder buildXmlHolder(GroovyUtils groovyUtils, String xml, @Nullable Map<String, String> xmlNamespaces) throws Exception {
        XmlHolder xmlHolder = groovyUtils.getXmlHolder(xml);

        DEFAULT_XML_NAMESPACES.forEach(xmlHolder::declareNamespace);

        if (!MapUtils.isEmpty(xmlNamespaces)) {
            xmlNamespaces.forEach(xmlHolder::declareNamespace);
        }

        return xmlHolder;
    }

    public GroovyUtils buildGroovyUtils(PropertyExpansionContext propExpansionContext) {
        return new GroovyUtils(propExpansionContext);
    }
}
