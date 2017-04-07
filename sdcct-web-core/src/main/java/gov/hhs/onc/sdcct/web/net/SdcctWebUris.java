package gov.hhs.onc.sdcct.web.net;

public final class SdcctWebUris {
    public final static String TESTCASES_PATH_PREFIX = "/testcases";
    public final static String TESTCASES_IHE_PATH_PREFIX = TESTCASES_PATH_PREFIX + "/ihe";
    public final static String TESTCASES_IHE_FORM_PATH_PREFIX = TESTCASES_IHE_PATH_PREFIX + "/Form";
    
    public final static String PROCESS_PATH_SUFFIX = "/process";
    
    public final static String TESTCASES_IHE_FORM_ARCHIVER_PROCESS_PATH = TESTCASES_IHE_FORM_PATH_PREFIX + "Archiver" + PROCESS_PATH_SUFFIX;
    public final static String TESTCASES_IHE_FORM_MANAGER_PROCESS_PATH = TESTCASES_IHE_FORM_PATH_PREFIX + "Manager" + PROCESS_PATH_SUFFIX;
    public final static String TESTCASES_IHE_FORM_RECEIVER_PROCESS_PATH = TESTCASES_IHE_FORM_PATH_PREFIX + "Receiver" + PROCESS_PATH_SUFFIX;

    private SdcctWebUris() {
    }
}
