package gov.hhs.onc.sdcct.ws.metadata;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import java.util.Map;

public interface WsMetadata<T extends InteractionWsMetadata, U extends ResourceWsMetadata<?, ?, T>>
    extends SpecifiedBean, WsMetadataComponent {
    public Map<String, U> getResourceMetadatas();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setResourceMetadatas(U ... resourceMetadatas);
}
