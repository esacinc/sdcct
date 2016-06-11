package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.xml.XmlDecodeOptions;
import net.sf.saxon.lib.ParseOptions;

public class XmlDecodeOptionsImpl extends AbstractXmlCodecOptions<XmlDecodeOptions> implements XmlDecodeOptions {
    private final static long serialVersionUID = 0L;

    public XmlDecodeOptionsImpl(ParseOptions parseOpts) {
        super(parseOpts);
    }

    @Override
    protected XmlDecodeOptions cloneInternal() {
        return new XmlDecodeOptionsImpl(parseOpts);
    }
}
