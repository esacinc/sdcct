package gov.hhs.onc.sdcct.rfd.ws;

import gov.hhs.onc.sdcct.net.SdcctUris;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;

public final class RfdWsaActions {
    public final static String PREFIX = SdcctUris.IHE_ITI_URN_VALUE + SdcctStringUtils.COLON;

    public final static String ARCHIVE_FORM = PREFIX + RfdWsXmlNames.ARCHIVE_FORM;
    public final static String ARCHIVE_FORM_RESP = PREFIX + RfdWsXmlNames.ARCHIVE_FORM_RESP;
    public final static String RETRIEVE_CLARIFICATION = PREFIX + RfdWsXmlNames.RETRIEVE_CLARIFICATION;
    public final static String RETRIEVE_CLARIFICATION_RESP = PREFIX + RfdWsXmlNames.RETRIEVE_CLARIFICATION_RESP;
    public final static String RETRIEVE_FORM = PREFIX + RfdWsXmlNames.RETRIEVE_FORM;
    public final static String RETRIEVE_FORM_RESP = PREFIX + RfdWsXmlNames.RETRIEVE_FORM_RESP;
    public final static String SUBMIT_FORM = PREFIX + RfdWsXmlNames.SUBMIT_FORM;
    public final static String SUBMIT_FORM_RESP = PREFIX + RfdWsXmlNames.SUBMIT_FORM_RESP;

    private RfdWsaActions() {
    }
}
