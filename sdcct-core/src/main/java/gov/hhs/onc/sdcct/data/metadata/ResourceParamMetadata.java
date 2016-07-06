package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.util.Set;
import javax.annotation.Nullable;

public interface ResourceParamMetadata extends ResourceMetadataComponent {
    public boolean hasBinding();

    @Nullable
    public ResourceParamBinding getBinding();

    public void setBinding(@Nullable ResourceParamBinding binding);

    public ResourceParamCardinality getCardinality();

    public void setCardinality(ResourceParamCardinality cardinality);

    public boolean hasExpression();

    @Nullable
    public String getExpression();

    public void setExpression(@Nullable String expr);

    public boolean isInline();

    public void setInline(boolean inline);

    public boolean isMeta();

    public void setMeta(boolean meta);

    public ResourceParamType getType();

    public Set<String> getValueTypes();

    public void setValueTypes(Set<String> valueTypes);

    public boolean hasXpathExecutable();

    @Nullable
    public SdcctXpathExecutable getXpathExecutable();

    public void setXpathExecutable(@Nullable SdcctXpathExecutable xpathExec);
}
