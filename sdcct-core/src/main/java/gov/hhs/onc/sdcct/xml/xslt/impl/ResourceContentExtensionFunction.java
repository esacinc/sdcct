package gov.hhs.onc.sdcct.xml.xslt.impl;

import gov.hhs.onc.sdcct.beans.factory.impl.EmbeddedPlaceholderResolver;
import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.transform.ResourceSourceResolver;
import gov.hhs.onc.sdcct.transform.SdcctTransformException;
import gov.hhs.onc.sdcct.transform.impl.ResourceSource;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import gov.hhs.onc.sdcct.xml.impl.AbstractSdcctExtensionFunction;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlUtils;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("extFuncResourceContent")
public class ResourceContentExtensionFunction extends AbstractSdcctExtensionFunction {
    public final static StructuredQName QNAME = new QName(SdcctXmlPrefixes.SDCCT_XML, SdcctUris.SDCCT_XML_URN_VALUE, "resource-content").getStructuredQName();

    @Autowired
    private ResourceSourceResolver resourceSrcResolver;

    @Autowired
    private EmbeddedPlaceholderResolver embeddedPlaceholderResolver;

    public ResourceContentExtensionFunction() {
        super(QNAME, SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING);
    }

    @Override
    protected Sequence callInternal(XPathContext context, Map<Object, Object> contextData, Sequence ... args) throws Exception {
        String loc = this.embeddedPlaceholderResolver.resolvePlaceholders(SdcctXmlUtils.getStringValue(args[0]));
        ResourceSource src = this.resourceSrcResolver.resolve(loc);

        if (src == null) {
            throw new SdcctTransformException(String.format("Unable to resolve resource (loc=%s).", loc));
        }

        return new StringValue(new String(src.getBytes(), StandardCharsets.UTF_8));
    }
}
