package gov.hhs.onc.sdcct.web.websocket;

public final class SdcctWebSocketPaths {
    public final static String APP_PREFIX = "/app";
    public final static String TOPIC_PREFIX = "/topic";

    public final static String TESTCASES_IHE_RESULTS = "/testcases/ihe/results";
    public final static String TOPIC_TESTCASES_IHE_RESULTS = TOPIC_PREFIX + TESTCASES_IHE_RESULTS;

    private SdcctWebSocketPaths() {
    }
}
