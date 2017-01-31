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
import net.sf.saxon.value.BooleanValue;
import net.sf.saxon.value.SequenceType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("extFuncIsBlank")
public class IsBlankExtensionFunction extends AbstractSdcctExtensionFunction {
    public final static StructuredQName QNAME = new QName(SdcctXmlPrefixes.SDCCT_XML, SdcctUris.SDCCT_XML_URN_VALUE, "is-blank").getStructuredQName();

    public IsBlankExtensionFunction() {
        super(QNAME, SequenceType.SINGLE_BOOLEAN, SequenceType.OPTIONAL_ATOMIC);
    }

    @Override
    protected Sequence callInternal(XPathContext context, Map<Object, Object> contextData, Sequence ... args) throws Exception {
        return BooleanValue.get(StringUtils.isBlank(SdcctXdmUtils.getStringValue(args[0])));
    }
}
