package gov.hhs.onc.sdcct.net.mime;

import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

public final class SdcctMediaTypes {
    public final static String APP_TYPE = "application";
    public final static String IMAGE_TYPE = "image";
    public final static String TEXT_TYPE = "text";

    public final static String FHIR_SUBTYPE_SUFFIX = "fhir";
    public final static String SDC_SUBTYPE_SUFFIX = "sdc";

    public final static String HTML_SUBTYPE = "html";
    public final static String HTML_SDC_SUBTYPE = HTML_SUBTYPE + SdcctStringUtils.PLUS + SDC_SUBTYPE_SUFFIX;
    public final static String JSON_SUBTYPE = "json";
    public final static String JSON_FHIR_SUBTYPE = JSON_SUBTYPE + SdcctStringUtils.PLUS + FHIR_SUBTYPE_SUFFIX;
    public final static String X_ICON_SUBTYPE = "x-icon";
    public final static String XML_SUBTYPE = "xml";
    public final static String WILDCARD_XML_SUBTYPE = javax.ws.rs.core.MediaType.MEDIA_TYPE_WILDCARD + SdcctStringUtils.PLUS + XML_SUBTYPE;
    public final static String XML_FHIR_SUBTYPE = XML_SUBTYPE + SdcctStringUtils.PLUS + FHIR_SUBTYPE_SUFFIX;
    public final static String XML_SDC_SUBTYPE = XML_SUBTYPE + SdcctStringUtils.PLUS + SDC_SUBTYPE_SUFFIX;

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

    public final static String APP_XML_SDC_VALUE = APP_TYPE + SdcctStringUtils.SLASH + XML_SDC_SUBTYPE;
    public final static MediaType APP_XML_SDC = new MediaType(APP_TYPE, XML_SDC_SUBTYPE);

    public final static String IMAGE_X_ICON_VALUE = IMAGE_TYPE + SdcctStringUtils.SLASH + X_ICON_SUBTYPE;
    public final static MediaType IMAGE_X_ICON = new MediaType(IMAGE_TYPE, X_ICON_SUBTYPE);

    public final static String TEXT_HTML_SDC_VALUE = TEXT_TYPE + SdcctStringUtils.SLASH + HTML_SDC_SUBTYPE;
    public final static MediaType TEXT_HTML_SDC = new MediaType(TEXT_TYPE, HTML_SDC_SUBTYPE);

    private SdcctMediaTypes() {
    }
}
