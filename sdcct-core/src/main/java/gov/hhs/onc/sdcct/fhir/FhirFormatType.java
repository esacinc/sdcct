package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.beans.ContentTypeBean;
import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;

public enum FhirFormatType implements ContentTypeBean {
    JSON(SdcctContentType.JSON, SdcctMediaTypes.APP_JSON_FHIR, SdcctMediaTypes.APP_JSON_FHIR_UTF8, SdcctMediaTypes.JSON_SUBTYPE),
    XML(SdcctContentType.XML, SdcctMediaTypes.APP_XML_FHIR, SdcctMediaTypes.APP_XML_FHIR_UTF8, SdcctMediaTypes.XML_SUBTYPE, MediaType.TEXT_XML_VALUE);

    private final Set<MediaType> compatMediaTypes;
    private final SdcctContentType contentType;
    private final MediaType mediaType;
    private final MediaType encMediaType;
    private final Set<String> queryParamValues;

    private FhirFormatType(SdcctContentType contentType, MediaType mediaType, MediaType encMediaType, String ... queryParamValues) {
        this.compatMediaTypes = Collections.singleton((this.mediaType = mediaType));
        this.encMediaType = encMediaType;
        this.queryParamValues =
            Stream.concat(Stream.<String>builder().add(this.mediaType.toString()).add((this.contentType = contentType).getMediaType().toString()).build(),
                Stream.of(queryParamValues)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<MediaType> getCompatibleMediaTypes() {
        return this.compatMediaTypes;
    }

    public SdcctContentType getContentType() {
        return this.contentType;
    }

    public MediaType getEncodedMediaType() {
        return this.encMediaType;
    }

    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }

    public Set<String> getQueryParamValues() {
        return this.queryParamValues;
    }
}
