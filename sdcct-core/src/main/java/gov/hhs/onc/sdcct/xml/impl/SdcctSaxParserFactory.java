package gov.hhs.onc.sdcct.xml.impl;

import com.ctc.wstx.sax.WstxSAXParser;
import com.ctc.wstx.sax.WstxSAXParserFactory;

public class SdcctSaxParserFactory extends WstxSAXParserFactory {
    public SdcctSaxParserFactory(SdcctXmlInputFactory xmlInFactory) {
        super(xmlInFactory);
    }

    @Override
    public WstxSAXParser newSAXParser() {
        return ((WstxSAXParser) super.newSAXParser());
    }
}
