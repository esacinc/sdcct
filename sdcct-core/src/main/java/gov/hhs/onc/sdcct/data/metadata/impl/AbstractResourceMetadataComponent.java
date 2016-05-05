package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataComponent;
import java.net.URI;
import javax.annotation.Nullable;

public abstract class AbstractResourceMetadataComponent extends AbstractMetadataComponent implements ResourceMetadataComponent {
    protected SpecificationType specType;
    protected String id;
    protected URI uri;

    public AbstractResourceMetadataComponent(SpecificationType specType, String id, String name, @Nullable URI uri) {
        super(name);

        this.specType = specType;
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    @Override
    public String getId() {
        return this.id;
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
