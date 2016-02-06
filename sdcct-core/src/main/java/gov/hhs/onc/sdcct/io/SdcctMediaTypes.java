package gov.hhs.onc.sdcct.io;

import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

public final class SdcctMediaTypes {
    public final static String DELIM = "/";
    public final static String SUBTYPE_DELIM = "+";

    public final static String APP_TYPE = "application";

    public final static String FHIR_SUBTYPE_SUFFIX = "fhir";

    public final static String JSON_SUBTYPE = "json";
    public final static String JSON_FHIR_SUBTYPE = JSON_SUBTYPE + SUBTYPE_DELIM + FHIR_SUBTYPE_SUFFIX;
    public final static String XML_SUBTYPE = "xml";
    public final static String WILDCARD_XML_SUBTYPE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + SUBTYPE_DELIM + XML_SUBTYPE;
    public final static String XML_FHIR_SUBTYPE = XML_SUBTYPE + SUBTYPE_DELIM + FHIR_SUBTYPE_SUFFIX;

    public final static String APP_JSON_FHIR_VALUE = APP_TYPE + DELIM + JSON_FHIR_SUBTYPE;
    public final static MediaType APP_JSON_FHIR = new MediaType(APP_TYPE, JSON_FHIR_SUBTYPE);
    public final static MediaType APP_JSON_FHIR_UTF8 = new MediaType(APP_TYPE, JSON_FHIR_SUBTYPE, StandardCharsets.UTF_8);

    public final static String WILDCARD_XML_VALUE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + DELIM + XML_SUBTYPE;
    public final static MediaType WILDCARD_XML = new MediaType(javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD, XML_SUBTYPE);

    public final static String WILDCARD_WILDCARD_XML_VALUE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + DELIM + WILDCARD_XML_SUBTYPE;
    public final static MediaType WILDCARD_WILDCARD_XML = new MediaType(javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD, WILDCARD_XML_SUBTYPE);

    public final static String APP_XML_FHIR_VALUE = APP_TYPE + DELIM + XML_FHIR_SUBTYPE;
    public final static MediaType APP_XML_FHIR = new MediaType(APP_TYPE, XML_FHIR_SUBTYPE);
    public final static MediaType APP_XML_FHIR_UTF8 = new MediaType(APP_TYPE, XML_FHIR_SUBTYPE, StandardCharsets.UTF_8);

    private SdcctMediaTypes() {
    }
}
