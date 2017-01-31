package gov.hhs.onc.sdcct.xml.xslt.saxon.impl;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import gov.hhs.onc.sdcct.xml.saxon.impl.AbstractSdcctExtensionFunction;
import gov.hhs.onc.sdcct.xml.saxon.utils.SdcctXdmUtils;
import java.util.Map;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("extFuncDefaultIfBlank")
public class DefaultIfBlankExtensionFunction extends AbstractSdcctExtensionFunction {
    public final static StructuredQName QNAME = new QName(SdcctXmlPrefixes.SDCCT_XML, SdcctUris.SDCCT_XML_URN_VALUE, "default-if-blank").getStructuredQName();

    public DefaultIfBlankExtensionFunction() {
        super(QNAME, SequenceType.SINGLE_STRING, SequenceType.OPTIONAL_ATOMIC, SequenceType.OPTIONAL_ATOMIC);
    }

    @Override
    protected Sequence callInternal(XPathContext context, Map<Object, Object> contextData, Sequence ... args) throws Exception {
        return StringValue.makeStringValue(StringUtils.defaultIfBlank(SdcctXdmUtils.getStringValue(args[0]), SdcctXdmUtils.getStringValue(args[1])));
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }
}
