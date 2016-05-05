package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.net.URI;
import javax.annotation.Nullable;

public class SearchParamMetadataImpl extends AbstractResourceMetadataComponent implements SearchParamMetadata {
    private SearchParamType type;
    private SdcctXpathExecutable xpathExec;
    private boolean inline;
    private URI valueSetUri;
    private String valueType;

    public SearchParamMetadataImpl(SpecificationType specType, String id, String name, @Nullable URI uri, SearchParamType type,
        @Nullable SdcctXpathExecutable xpathExec, boolean inline) {
        super(specType, id, name, uri);

        this.type = type;
        this.xpathExec = xpathExec;
        this.inline = inline;
    }

    @Override
    public boolean isInline() {
        return this.inline;
    }

    @Override
    public SearchParamType getType() {
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
