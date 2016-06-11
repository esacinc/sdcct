package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import net.sf.saxon.lib.ParseOptions;

public class XmlEncodeOptionsImpl extends AbstractXmlCodecOptions<XmlEncodeOptions> implements XmlEncodeOptions {
    private final static long serialVersionUID = 0L;

    public XmlEncodeOptionsImpl(ParseOptions parseOpts) {
        super(parseOpts);
    }

    @Override
    protected XmlEncodeOptions cloneInternal() {
        return new XmlEncodeOptionsImpl(parseOpts);
    }
}
