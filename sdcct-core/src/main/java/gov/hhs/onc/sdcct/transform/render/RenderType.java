package gov.hhs.onc.sdcct.transform.render;

import gov.hhs.onc.sdcct.io.SdcctFileNameExtensions;
import org.springframework.http.MediaType;

public enum RenderType {
    HTML(MediaType.TEXT_HTML, SdcctFileNameExtensions.HTML), JSON(MediaType.APPLICATION_JSON, SdcctFileNameExtensions.JSON), XML(MediaType.APPLICATION_XML,
        SdcctFileNameExtensions.XML);

    private final MediaType mediaType;
    private final String fileNameExt;

    private RenderType(MediaType mediaType, String fileNameExt) {
        this.mediaType = mediaType;
        this.fileNameExt = fileNameExt;
    }

    public String getFileNameExtension() {
        return this.fileNameExt;
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }
}
