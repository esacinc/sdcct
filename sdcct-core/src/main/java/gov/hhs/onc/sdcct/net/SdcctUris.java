package gov.hhs.onc.sdcct.net;

import gov.hhs.onc.sdcct.net.utils.SdcctUriUtils;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.net.URI;

public final class SdcctUris {
    public final static String IETF_URN_NID = "ietf";
    public final static String IHE_URN_NID = "ihe";
    public final static String OID_URN_NID = "oid";
    public final static String SDCCT_URN_NID = "sdcct";
    public final static String UUID_URN_NID = "uuid";

    public final static String HTTP_URL_PREFIX = SdcctSchemes.HTTP + SdcctUriUtils.HIERARCHICAL_DELIM;
    public final static String SCHEMATRON_URL_PREFIX = HTTP_URL_PREFIX + "purl.oclc.org/dsdl/";

    public final static String URN_PREFIX = SdcctSchemes.URN + SdcctStringUtils.COLON;
    public final static String IETF_URN_PREFIX = URN_PREFIX + IETF_URN_NID + SdcctStringUtils.COLON;
    public final static String IHE_URN_PREFIX = URN_PREFIX + IHE_URN_NID + SdcctStringUtils.COLON;
    public final static String OID_URN_PREFIX = URN_PREFIX + OID_URN_NID + SdcctStringUtils.COLON;
    public final static String SDCCT_URN_PREFIX = URN_PREFIX + SDCCT_URN_NID + SdcctStringUtils.COLON;
    public final static String UUID_URN_PREFIX = URN_PREFIX + UUID_URN_NID + SdcctStringUtils.COLON;

    public final static String FHIR_URL_VALUE = HTTP_URL_PREFIX + "hl7.org/fhir";
    public final static URI FHIR_URL = URI.create(FHIR_URL_VALUE);

    public final static String SDC_URL_VALUE = HTTP_URL_PREFIX + "healthIT.gov/sdc";
    public final static URI SDC_URL = URI.create(SDC_URL_VALUE);

    public final static String IETF_RFC_3986_URN_NSS = "rfc:3986";
    public final static String IETF_RFC_3986_URN_VALUE = IETF_URN_PREFIX + IETF_RFC_3986_URN_NSS;
    public final static URI IETF_RFC_3986_URN = URI.create(IETF_RFC_3986_URN_VALUE);

    public final static String IHE_ITI_URN_NSS = "iti:2007";
    public final static String IHE_ITI_URN_VALUE = IHE_URN_PREFIX + IHE_ITI_URN_NSS;
    public final static URI IHE_ITI_URN = URI.create(IHE_ITI_URN_VALUE);

    public final static String IHE_ITI_RFD_URN_NSS = "iti:rfd:2007";
    public final static String IHE_ITI_RFD_URN_VALUE = IHE_URN_PREFIX + IHE_ITI_RFD_URN_NSS;
    public final static URI IHE_ITI_RFD_URN = URI.create(IHE_ITI_RFD_URN_VALUE);

    public final static String SCHEMATRON_URL_VALUE = SCHEMATRON_URL_PREFIX + "schematron";
    public final static URI SCHEMATRON_URL = URI.create(SCHEMATRON_URL_VALUE);

    public final static String SCHEMATRON_SVRL_URL_VALUE = SCHEMATRON_URL_PREFIX + "svrl";
    public final static URI SCHEMATRON_SVRL_URL = URI.create(SCHEMATRON_SVRL_URL_VALUE);

    public final static String SDCCT_CORE_URN_NSS = "core";
    public final static String SDCCT_CORE_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_CORE_URN_NSS;
    public final static URI SDCCT_CORE_URN = URI.create(SDCCT_CORE_URN_VALUE);

    public final static String SDCCT_FHIR_URN_NSS = "fhir";
    public final static String SDCCT_FHIR_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_FHIR_URN_NSS;
    public final static URI SDCCT_FHIR_URN = URI.create(SDCCT_FHIR_URN_VALUE);

    public final static String SDCCT_SPRING_URN_NSS = "spring";
    public final static String SDCCT_SPRING_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_SPRING_URN_NSS;
    public final static URI SDCCT_SPRING_URN = URI.create(SDCCT_SPRING_URN_VALUE);

    public final static String SDCCT_SPRING_XML_URN_NSS = SDCCT_SPRING_URN_NSS + ":xml";
    public final static String SDCCT_SPRING_XML_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_SPRING_XML_URN_NSS;
    public final static URI SDCCT_SPRING_XML_URN = URI.create(SDCCT_SPRING_XML_URN_VALUE);

    public final static String SDCCT_WS_URN_NSS = "ws";
    public final static String SDCCT_WS_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_WS_URN_NSS;
    public final static URI SDCCT_WS_URN = URI.create(SDCCT_WS_URN_VALUE);

    public final static String SDCCT_XML_URN_NSS = "xml";
    public final static String SDCCT_XML_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_XML_URN_NSS;
    public final static URI SDCCT_XML_URN = URI.create(SDCCT_XML_URN_VALUE);

    public final static String SDCCT_XML_JAXB_URN_NSS = SDCCT_XML_URN_NSS + ":jaxb";
    public final static String SDCCT_XML_JAXB_URN_VALUE = SDCCT_URN_PREFIX + SDCCT_XML_JAXB_URN_NSS;
    public final static URI SDCCT_XML_JAXB_URN = URI.create(SDCCT_XML_JAXB_URN_VALUE);

    private SdcctUris() {
    }
}
