package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.xml.SdcctXmlNs;

public final class RfdWsaActions {
    public final static String PREFIX = SdcctXmlNs.IHE_ITI_URI + ":";

    public final static String ARCHIVE_FORM = PREFIX + RfdXmlNames.ARCHIVE_FORM;
    public final static String ARCHIVE_FORM_RESP = PREFIX + RfdXmlNames.ARCHIVE_FORM_RESP;
    public final static String RETRIEVE_CLARIFICATION = PREFIX + RfdXmlNames.RETRIEVE_CLARIFICATION;
    public final static String RETRIEVE_CLARIFICATION_RESP = PREFIX + RfdXmlNames.RETRIEVE_CLARIFICATION_RESP;
    public final static String RETRIEVE_FORM = PREFIX + RfdXmlNames.RETRIEVE_FORM;
    public final static String RETRIEVE_FORM_RESP = PREFIX + RfdXmlNames.RETRIEVE_FORM_RESP;
    public final static String SUBMIT_FORM = PREFIX + RfdXmlNames.SUBMIT_FORM;
    public final static String SUBMIT_FORM_RESP = PREFIX + RfdXmlNames.SUBMIT_FORM_RESP;

    private RfdWsaActions() {
    }
}
