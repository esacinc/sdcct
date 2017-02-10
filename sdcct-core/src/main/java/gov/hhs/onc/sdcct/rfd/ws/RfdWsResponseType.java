package gov.hhs.onc.sdcct.rfd.ws;

import gov.hhs.onc.sdcct.beans.ContentTypeBean;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.net.mime.SdcctMediaTypes;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.http.MediaType;

public enum RfdWsResponseType implements ContentTypeBean, IdentifiedBean {
    XML(null, SdcctMediaTypes.APP_XML_SDC), HTML(null, SdcctMediaTypes.TEXT_HTML_SDC), URL("URL", null);

    private final String id;
    private final MediaType mediaType;
    private final Set<MediaType> compatMediaTypes;

    private RfdWsResponseType(@Nullable String id, @Nullable MediaType mediaType) {
        this.id = ((id != null) ? id : this.name());
        this.mediaType = mediaType;
        this.compatMediaTypes = ((mediaType != null) ? Collections.singleton(this.mediaType) : Collections.emptySet());
    }

    @Override
    public Set<MediaType> getCompatibleMediaTypes() {
        return this.compatMediaTypes;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public boolean hasMediaType() {
        return (this.mediaType != null);
    }

    @Nullable
    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }
}
