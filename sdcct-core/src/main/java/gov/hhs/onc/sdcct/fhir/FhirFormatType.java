package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;

public enum FhirFormatType {
    JSON(SdcctContentType.JSON, SdcctMediaTypes.APP_JSON_FHIR, SdcctMediaTypes.APP_JSON_FHIR_UTF8, SdcctMediaTypes.JSON_SUBTYPE),
    XML(SdcctContentType.XML, SdcctMediaTypes.APP_XML_FHIR, SdcctMediaTypes.APP_XML_FHIR_UTF8, SdcctMediaTypes.XML_SUBTYPE, MediaType.TEXT_XML_VALUE);

    private final SdcctContentType contentType;
    private final MediaType mediaType;
    private final MediaType encMediaType;
    private final Set<String> queryParamValues;

    private FhirFormatType(SdcctContentType contentType, MediaType mediaType, MediaType encMediaType, String ... queryParamValues) {
        (this.queryParamValues = Stream.of(queryParamValues).collect(Collectors.toCollection(() -> new LinkedHashSet<>((queryParamValues.length + 2)))))
            .add((this.contentType = contentType).getMediaType().toString());
        this.queryParamValues.add((this.mediaType = mediaType).toString());

        this.encMediaType = encMediaType;
    }

    public SdcctContentType getContentType() {
        return this.contentType;
    }

    public MediaType getEncodedMediaType() {
        return this.encMediaType;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public Set<String> getQueryParamValues() {
        return this.queryParamValues;
    }
}
