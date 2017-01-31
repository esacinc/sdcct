package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import gov.hhs.onc.sdcct.xml.qname.utils.SdcctQnameUtils;
import javax.xml.namespace.QName;

public final class WsXmlQnames {
    public final static QName STACK_TRACE = SdcctQnameUtils.build(SdcctXmlPrefixes.SDCCT_WS, SdcctUris.SDCCT_WS_URN_VALUE, WsXmlNames.STACK_TRACE);

    private WsXmlQnames() {
    }
}
