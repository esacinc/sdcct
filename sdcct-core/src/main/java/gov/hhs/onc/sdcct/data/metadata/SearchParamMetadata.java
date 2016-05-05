package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.net.URI;
import javax.annotation.Nullable;

public interface SearchParamMetadata extends ResourceMetadataComponent {
    public boolean isInline();

    public SearchParamType getType();

    public boolean hasValueSetUri();

    @Nullable
    public URI getValueSetUri();

    public void setValueSetUri(@Nullable URI valueSetUri);

    public String getValueType();

    public void setValueType(String valueType);

    public boolean hasXpathExecutable();

    @Nullable
    public SdcctXpathExecutable getXpathExecutable();
}
