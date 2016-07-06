package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamBinding;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamCardinality;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.util.Set;
import javax.annotation.Nullable;

public class ResourceParamMetadataImpl extends AbstractResourceMetadataComponent implements ResourceParamMetadata {
    private ResourceParamType type;
    private boolean inline;
    private boolean meta;
    private String expr;
    private SdcctXpathExecutable xpathExec;
    private ResourceParamBinding binding;
    private ResourceParamCardinality cardinality;
    private Set<String> valueTypes;

    public ResourceParamMetadataImpl(SpecificationType specType, ResourceParamType type, String id, String name) {
        super(specType, id, name);

        this.type = type;
    }

    public boolean hasBinding() {
        return (this.binding != null);
    }

    @Nullable
    @Override
    public ResourceParamBinding getBinding() {
        return this.binding;
    }

    @Override
    public void setBinding(@Nullable ResourceParamBinding binding) {
        this.binding = binding;
    }

    @Override
    public ResourceParamCardinality getCardinality() {
        return this.cardinality;
    }

    @Override
    public void setCardinality(ResourceParamCardinality cardinality) {
        this.cardinality = cardinality;
    }

    @Override
    public boolean hasExpression() {
        return (this.expr != null);
    }

    @Override
    public String getExpression() {
        return this.expr;
    }

    @Override
    public void setExpression(String expr) {
        this.expr = expr;
    }

    @Override
    public boolean isInline() {
        return this.inline;
    }

    @Override
    public void setInline(boolean inline) {
        this.inline = inline;
    }

    @Override
    public boolean isMeta() {
        return this.meta;
    }

    @Override
    public void setMeta(boolean meta) {
        this.meta = meta;
    }

    @Override
    public ResourceParamType getType() {
        return this.type;
    }

    @Override
    public Set<String> getValueTypes() {
        return this.valueTypes;
    }

    @Override
    public void setValueTypes(Set<String> valueTypes) {
        this.valueTypes = valueTypes;
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

    @Override
    public void setXpathExecutable(@Nullable SdcctXpathExecutable xpathExec) {
        this.xpathExec = xpathExec;
    }
}
