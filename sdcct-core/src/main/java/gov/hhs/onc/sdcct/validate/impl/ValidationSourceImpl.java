package gov.hhs.onc.sdcct.validate.impl;

import gov.hhs.onc.sdcct.validate.ValidationSource;
import java.net.URI;
import javax.annotation.Nullable;

public class ValidationSourceImpl implements ValidationSource {
    private String id;
    private String name;
    private URI uri;

    public ValidationSourceImpl(String id, String name, @Nullable URI uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
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
}
