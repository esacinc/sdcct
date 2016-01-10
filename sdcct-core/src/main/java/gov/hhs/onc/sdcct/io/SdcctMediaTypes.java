package gov.hhs.onc.sdcct.io;

import org.springframework.http.MediaType;

public final class SdcctMediaTypes {
    public final static String DELIM = "/";
    public final static String SUBTYPE_DELIM = "+";

    public final static String APP_TYPE = "application";

    public final static String FHIR_SUBTYPE_SUFFIX = "fhir";

    public final static String APP_JSON_FHIR_VALUE = MediaType.APPLICATION_JSON_VALUE + SUBTYPE_DELIM + FHIR_SUBTYPE_SUFFIX;
    public final static MediaType APP_JSON_FHIR = MediaType.parseMediaType(APP_JSON_FHIR_VALUE);

    public final static String APP_XML_FHIR_VALUE = MediaType.APPLICATION_XML_VALUE + SUBTYPE_DELIM + FHIR_SUBTYPE_SUFFIX;
    public final static MediaType APP_XML_FHIR = MediaType.parseMediaType(APP_XML_FHIR_VALUE);

    private SdcctMediaTypes() {
    }
}
