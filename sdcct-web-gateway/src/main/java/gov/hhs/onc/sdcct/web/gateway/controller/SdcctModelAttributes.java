package gov.hhs.onc.sdcct.web.gateway.controller;

public final class SdcctModelAttributes {
    public final static String FORMATTED_BUILD_TIMESTAMP_NAME = "formattedBuildTimestamp";
    public final static String BUILD_VERSION_NAME = "buildVersion";

    public final static String IHE_TESTCASES_NAME = "iheTestcases";
    public final static String IHE_TESTCASES_EVENT_POLL_URL_NAME = IHE_TESTCASES_NAME + "EventPollUrl";
    public final static String IHE_TESTCASES_PROCESS_URLS_NAME = IHE_TESTCASES_NAME + "ProcessUrls";
    public final static String IHE_ENDPOINT_ADDRESSES_NAME = "iheEndpointAddresses";

    private SdcctModelAttributes() {
    }
}
