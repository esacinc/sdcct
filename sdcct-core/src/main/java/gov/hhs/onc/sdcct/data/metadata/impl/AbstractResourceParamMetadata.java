package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.net.URI;
import javax.annotation.Nullable;

public abstract class AbstractResourceParamMetadata<T extends Enum<T>> extends AbstractResourceMetadataComponent implements ResourceParamMetadata<T> {
    protected T type;
    protected URI valueSetUri;
    protected String valueType;
    protected SdcctXpathExecutable xpathExec;

    protected AbstractResourceParamMetadata(SpecificationType specType, String id, String name, @Nullable URI uri, T type,
        @Nullable SdcctXpathExecutable xpathExec) {
        super(specType, id, name, uri);

        this.type = type;
        this.xpathExec = xpathExec;
    }

    @Override
    public T getType() {
        return this.type;
    }

    @Override
    public boolean hasValueSetUri() {
        return (this.valueSetUri != null);
    }

    @Nullable
    @Override
    public URI getValueSetUri() {
        return this.valueSetUri;
    }

    @Override
    public void setValueSetUri(@Nullable URI valueSetUri) {
        this.valueSetUri = valueSetUri;
    }

    @Override
    public String getValueType() {
        return this.valueType;
    }

    @Override
    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public boolean hasXpathExecutable() {
        return (this.xpathExec != null);
    }

    @Nullable
    @Override
    public SdcctXpathExecutable getXpathExecutable() {
        return this.xpathExec;
    }
}
