package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataComponent;
import gov.hhs.onc.sdcct.metadata.impl.AbstractMetadataComponent;
import java.net.URI;
import javax.annotation.Nullable;

public abstract class AbstractResourceMetadataComponent extends AbstractMetadataComponent implements ResourceMetadataComponent {
    protected SpecificationType specType;
    protected String id;
    protected URI uri;

    protected AbstractResourceMetadataComponent(SpecificationType specType, String id, String name) {
        super(name);

        this.specType = specType;
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }

    @Override
    public boolean hasUri() {
        return (this.uri != null);
    }

    @Nullable
    @Override
    public URI getUri() {
        return this.uri;
    }

    @Override
    public void setUri(@Nullable URI uri) {
        this.uri = uri;
    }
}
