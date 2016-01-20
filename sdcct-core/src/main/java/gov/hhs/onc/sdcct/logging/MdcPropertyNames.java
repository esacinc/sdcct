package gov.hhs.onc.sdcct.logging;

public final class MdcPropertyNames {
    public final static String PREFIX = "sdcct.logging.mdc.";
    public final static String HTTP_PREFIX = PREFIX + "http.";
    public final static String WS_PREFIX = PREFIX + "ws.";

    public final static String TX_ID_SUFFIX = "tx.id";

    public final static String HTTP_TX_ID = HTTP_PREFIX + TX_ID_SUFFIX;
    public final static String WS_TX_ID = WS_PREFIX + TX_ID_SUFFIX;

    private MdcPropertyNames() {
    }
}
