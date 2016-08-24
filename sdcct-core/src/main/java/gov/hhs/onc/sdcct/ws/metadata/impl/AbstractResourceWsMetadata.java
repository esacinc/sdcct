package gov.hhs.onc.sdcct.ws.metadata.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.ws.WsInteractionType;
import gov.hhs.onc.sdcct.ws.metadata.InteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public abstract class AbstractResourceWsMetadata<T, U extends ResourceMetadata<T>, V extends InteractionWsMetadata> extends AbstractWsMetadataComponent
    implements ResourceWsMetadata<T, U, V> {
    protected U resourceMetadata;
    protected String path;
    protected Map<WsInteractionType, V> interactionMetadatas = new TreeMap<>();

    protected AbstractResourceWsMetadata(U resourceMetadata) {
        super(resourceMetadata.getId(), resourceMetadata.getName());

        this.path = (this.resourceMetadata = resourceMetadata).getPath();
    }

    @Override
    public Map<WsInteractionType, V> getInteractionMetadatas() {
        return this.interactionMetadatas;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setInteractionMetadatas(V ... interactionMetadatas) {
        this.interactionMetadatas.clear();

        Stream.of(interactionMetadatas).forEach(interactionMetadata -> this.interactionMetadatas.put(interactionMetadata.getType(), interactionMetadata));
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public U getResourceMetadata() {
        return this.resourceMetadata;
    }
}
