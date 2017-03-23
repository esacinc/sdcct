package gov.hhs.onc.sdcct.web.controller;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class PathNames {
    public final static String IHE = "ihe";
    public final static String EVENT = "event";
    public final static String PROCESS = "process";
    public final static String POLL = "poll";

    public final static String IHE_EVENT_POLL = IHE + SdcctStringUtils.SLASH + EVENT + SdcctStringUtils.SLASH + POLL;

    public final static String FORM_ARCHIVER = "FormArchiver";
    public final static String FORM_MANAGER = "FormManager";
    public final static String FORM_RECEIVER = "FormReceiver";

    public final static String FORM_ARCHIVER_EVENT = FORM_ARCHIVER + SdcctStringUtils.SLASH + EVENT;
    public final static String FORM_MANAGER_EVENT = FORM_MANAGER + SdcctStringUtils.SLASH + EVENT;
    public final static String FORM_RECEIVER_EVENT = FORM_RECEIVER + SdcctStringUtils.SLASH + EVENT;

    public final static String FORM_ARCHIVER_PROCESS = FORM_ARCHIVER + SdcctStringUtils.SLASH + PROCESS;
    public final static String FORM_MANAGER_PROCESS = FORM_MANAGER + SdcctStringUtils.SLASH + PROCESS;
    public final static String FORM_RECEIVER_PROCESS = FORM_RECEIVER + SdcctStringUtils.SLASH + PROCESS;

    private PathNames() {
    }
}
