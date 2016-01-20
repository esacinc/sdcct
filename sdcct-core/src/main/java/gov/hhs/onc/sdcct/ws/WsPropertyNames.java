package gov.hhs.onc.sdcct.ws;

public final class WsPropertyNames {
    public final static String PREFIX = "sdcct.ws.";
    public final static String HTTP_PREFIX = PREFIX + "http.";
    public final static String WS_PREFIX = PREFIX + "ws.";

    public final static String TX_ID_SUFFIX = "tx.id";

    public final static String ERROR_STACK_TRACE = PREFIX + "error.stack.trace";
    public final static String HTTP_TX_ID = HTTP_PREFIX + TX_ID_SUFFIX;
    public final static String WS_TX_ID = WS_PREFIX + TX_ID_SUFFIX;

    private WsPropertyNames() {
    }
}
