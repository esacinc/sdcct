package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.xml.SdcctXmlPrefixes;
import javax.xml.namespace.QName;

public final class WsXmlQnames {
    public final static QName STACK_TRACE = new QName(SdcctUris.SDCCT_SOAP_URN_VALUE, WsXmlNames.STACK_TRACE, SdcctXmlPrefixes.SDCCT_SOAP);

    private WsXmlQnames() {
    }
}
