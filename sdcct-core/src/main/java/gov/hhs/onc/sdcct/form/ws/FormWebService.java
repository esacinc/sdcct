package gov.hhs.onc.sdcct.form.ws;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.search.SearchService;
import gov.hhs.onc.sdcct.ws.metadata.InteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.WsMetadata;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface FormWebService<T extends SdcctResource, U extends SdcctResourceRegistry<?, ?, T>, V extends SearchService<?, ?, T, ?>, W extends InteractionWsMetadata, X extends ResourceWsMetadata<?, ?, W>, Y extends WsMetadata<W, X>>
    extends InitializingBean {
    public Y getMetadata();

    public void setMetadata(Y metadata);

    public Map<Class<?>, U> getResourceRegistries();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setResourceRegistries(U ... resourceRegistries);

    public Map<Class<?>, V> getSearchServices();

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setSearchServices(V ... searchServices);
}
