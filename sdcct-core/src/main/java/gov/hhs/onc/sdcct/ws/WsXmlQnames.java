package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.xml.SdcctXmlNs;
import javax.xml.namespace.QName;

public final class WsXmlQnames {
    public final static QName STACK_TRACE = new QName(SdcctXmlNs.SDCCT_URI, WsXmlNames.STACK_TRACE, SdcctXmlNs.SDCCT_PREFIX);

    private WsXmlQnames() {
    }
}
