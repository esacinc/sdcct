package gov.hhs.onc.sdcct.web.controller;

public final class ControllerPaths {
    public final static String PROCESS_SUFFIX = "/process";

    public final static String FORM_ARCHIVER_PROCESS = "/FormArchiver" + PROCESS_SUFFIX;
    public final static String FORM_MANAGER_PROCESS = "/FormManager" + PROCESS_SUFFIX;
    public final static String FORM_RECEIVER_PROCESS = "/FormReceiver" + PROCESS_SUFFIX;
    public final static String IHE = "/ihe";
    public final static String RESULTS_POLL = "/results/poll";

    private ControllerPaths() {
    }
}
