package gov.hhs.onc.sdcct.ws.metadata.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.ws.metadata.InteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.WsMetadata;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public abstract class AbstractWsMetadata<T extends InteractionWsMetadata, U extends ResourceWsMetadata<?, ?, T>> extends AbstractWsMetadataComponent
    implements WsMetadata<T, U> {
    protected SpecificationType specType;
    protected Map<String, U> resourceMetadatas = new TreeMap<>();

    protected AbstractWsMetadata(SpecificationType specType, String id, String name) {
        super(id, name);

        this.specType = specType;
    }

    @Override
    public Map<String, U> getResourceMetadatas() {
        return this.resourceMetadatas;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setResourceMetadatas(U ... resourceMetadatas) {
        this.resourceMetadatas.clear();

        Stream.of(resourceMetadatas)
            .forEach(resourceMetadata -> this.resourceMetadatas.put(resourceMetadata.getResourceMetadata().getPath(), resourceMetadata));
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
