package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import gov.hhs.onc.sdcct.xml.utils.SdcctXmlQnameUtils;
import net.sf.saxon.s9api.QName;

public final class WsXmlQnames {
    public final static QName STACK_TRACE = SdcctXmlQnameUtils.build(SdcctXmlPrefixes.SDCCT_WS, SdcctUris.SDCCT_WS_URN_VALUE, WsXmlNames.STACK_TRACE);

    private WsXmlQnames() {
    }
}
