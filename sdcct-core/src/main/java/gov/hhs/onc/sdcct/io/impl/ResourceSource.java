package gov.hhs.onc.sdcct.io.impl;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

public class ResourceSource extends ByteArraySource {
    private Resource resource;

    public ResourceSource(Resource resource) throws IOException {
        this(resource, null);
    }

    public ResourceSource(Resource resource, @Nullable String sysId) throws IOException {
        this(resource, null, sysId);
    }

    public ResourceSource(Resource resource, @Nullable String publicId, @Nullable String sysId) throws IOException {
        super(publicId, ((sysId != null) ? sysId : resource.getURL().toExternalForm()));

        try (InputStream resourceInStream = (this.resource = resource).getInputStream()) {
            this.bytes = IOUtils.toByteArray(resourceInStream);
        }
    }

    public Resource getResource() {
        return this.resource;
    }
}
