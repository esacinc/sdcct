package gov.hhs.onc.sdcct.io;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

public final class SdcctMediaTypes {
    public final static String APP_TYPE = "application";

    public final static String FHIR_SUBTYPE_SUFFIX = "fhir";

    public final static String JSON_SUBTYPE = "json";
    public final static String JSON_FHIR_SUBTYPE = JSON_SUBTYPE + SdcctStringUtils.PLUS + FHIR_SUBTYPE_SUFFIX;
    public final static String XML_SUBTYPE = "xml";
    public final static String WILDCARD_XML_SUBTYPE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + SdcctStringUtils.PLUS + XML_SUBTYPE;
    public final static String XML_FHIR_SUBTYPE = XML_SUBTYPE + SdcctStringUtils.PLUS + FHIR_SUBTYPE_SUFFIX;

    public final static String APP_JSON_FHIR_VALUE = APP_TYPE + SdcctStringUtils.SLASH + JSON_FHIR_SUBTYPE;
    public final static MediaType APP_JSON_FHIR = new MediaType(APP_TYPE, JSON_FHIR_SUBTYPE);
    public final static MediaType APP_JSON_FHIR_UTF8 = new MediaType(APP_TYPE, JSON_FHIR_SUBTYPE, StandardCharsets.UTF_8);

    public final static String WILDCARD_XML_VALUE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + SdcctStringUtils.SLASH + XML_SUBTYPE;
    public final static MediaType WILDCARD_XML = new MediaType(javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD, XML_SUBTYPE);

    public final static String WILDCARD_WILDCARD_XML_VALUE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + SdcctStringUtils.SLASH + WILDCARD_XML_SUBTYPE;
    public final static MediaType WILDCARD_WILDCARD_XML = new MediaType(javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD, WILDCARD_XML_SUBTYPE);

    public final static String APP_XML_FHIR_VALUE = APP_TYPE + SdcctStringUtils.SLASH + XML_FHIR_SUBTYPE;
    public final static MediaType APP_XML_FHIR = new MediaType(APP_TYPE, XML_FHIR_SUBTYPE);
    public final static MediaType APP_XML_FHIR_UTF8 = new MediaType(APP_TYPE, XML_FHIR_SUBTYPE, StandardCharsets.UTF_8);

    private SdcctMediaTypes() {
    }
}
