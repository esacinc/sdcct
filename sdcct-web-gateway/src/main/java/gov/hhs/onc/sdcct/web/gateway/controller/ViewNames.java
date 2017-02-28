package gov.hhs.onc.sdcct.web.gateway.controller;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class ViewNames {
    public final static String HOME = "home";
    public final static String IHE = "ihe";
    public final static String PROCESS = "process";

    public final static String FORM_ARCHIVER = "FormArchiver";
    public final static String FORM_MANAGER = "FormManager";
    public final static String FORM_RECEIVER = "FormReceiver";

    public final static String FORM_ARCHIVER_PROCESS = FORM_ARCHIVER + SdcctStringUtils.SLASH + PROCESS;
    public final static String FORM_MANAGER_PROCESS = FORM_MANAGER + SdcctStringUtils.SLASH + PROCESS;
    public final static String FORM_RECEIVER_PROCESS = FORM_RECEIVER + SdcctStringUtils.SLASH + PROCESS;

    private ViewNames() {
    }
}
