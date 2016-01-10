package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.io.SdcctMediaTypes;
import gov.hhs.onc.sdcct.transform.render.RenderType;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;

public enum FhirFormatType {
    JSON(RenderType.JSON, SdcctMediaTypes.APP_JSON_FHIR, "json"), XML(RenderType.XML, SdcctMediaTypes.APP_XML_FHIR, "xml", MediaType.TEXT_XML_VALUE);

    private final RenderType renderType;
    private final MediaType mediaType;
    private final Set<String> values;

    private FhirFormatType(RenderType renderType, MediaType mediaType, String ... values) {
        (this.values = Stream.of(values).collect(Collectors.toCollection(() -> new LinkedHashSet<>(values.length)))).add((this.renderType = renderType)
            .getMediaType().toString());
        this.values.add((this.mediaType = mediaType).toString());
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public RenderType getRenderType() {
        return this.renderType;
    }

    public Set<String> getValues() {
        return this.values;
    }
}
