package gov.hhs.onc.sdcct.ws.metadata;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.PathBean;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.ws.WsInteractionType;
import java.util.Map;

public interface ResourceWsMetadata<T, U extends ResourceMetadata<T>, V extends InteractionWsMetadata> extends PathBean, WsMetadataComponent {
    public Map<WsInteractionType, V> getInteractionMetadatas();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setInteractionMetadatas(V ... interactionMetadatas);

    public U getResourceMetadata();
}
