package gov.hhs.onc.sdcct.web.gateway.controller;

public final class SdcctModelAttributes {
    public final static String BUILD_NAME_PREFIX = "build";
    public final static String TESTCASES_IHE_RESULTS_NAME_PREFIX = SdcctModelAttributes.TESTCASES_IHE_NAME + "Results";

    public final static String ENDPOINT_NAME_SUFFIX = "Endpoint";
    public final static String WEBSOCKET_NAME_SUFFIX = "WebSocket";

    public final static String BUILD_TIMESTAMP_NAME = BUILD_NAME_PREFIX + "Timestamp";
    public final static String BUILD_VERSION_NAME = BUILD_NAME_PREFIX + "Version";
    public final static String TESTCASES_IHE_NAME = "testcasesIhe";
    public final static String TESTCASES_IHE_ENDPOINT_ADDRESSES_NAME = TESTCASES_IHE_NAME + ENDPOINT_NAME_SUFFIX + "Addresses";
    public final static String TESTCASES_IHE_PROCESS_URLS_NAME = TESTCASES_IHE_NAME + "ProcessUrls";
    public final static String TESTCASES_IHE_RESULTS_TOPIC_WEBSOCKET_ENDPOINT_NAME =
        TESTCASES_IHE_RESULTS_NAME_PREFIX + "Topic" + WEBSOCKET_NAME_SUFFIX + ENDPOINT_NAME_SUFFIX;
    public final static String TESTCASES_IHE_RESULTS_WEBSOCKET_URL_NAME = TESTCASES_IHE_RESULTS_NAME_PREFIX + WEBSOCKET_NAME_SUFFIX + "Url";

    private SdcctModelAttributes() {
    }
}
