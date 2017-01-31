package gov.hhs.onc.sdcct.transform.content;

import gov.hhs.onc.sdcct.beans.ContentTypeBean;
import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;

public enum SdcctContentType implements ContentTypeBean {
    HTML(MediaType.TEXT_HTML, SdcctFileNameExtensions.HTML, MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML),
    JSON(MediaType.APPLICATION_JSON, SdcctFileNameExtensions.JSON, MediaType.APPLICATION_JSON),
    XML(MediaType.APPLICATION_XML, SdcctFileNameExtensions.XML, SdcctMediaTypes.WILDCARD_XML, SdcctMediaTypes.WILDCARD_WILDCARD_XML);

    private final MediaType mediaType;
    private final String fileNameExt;
    private final Set<MediaType> compatMediaTypes;

    private SdcctContentType(MediaType mediaType, String fileNameExt, MediaType ... compatMediaTypes) {
        this.mediaType = mediaType;
        this.fileNameExt = fileNameExt;
        this.compatMediaTypes = Stream.of(compatMediaTypes).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Set<MediaType> getCompatibleMediaTypes() {
        return this.compatMediaTypes;
    }

    public String getFileNameExtension() {
        return this.fileNameExt;
    }

    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }
}
