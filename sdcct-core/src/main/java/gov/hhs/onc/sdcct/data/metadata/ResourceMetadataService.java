package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.SpecifiedBean;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;

public interface ResourceMetadataService<T, U extends ResourceMetadata<?>> extends InitializingBean, SpecifiedBean {
    public ResourceParamMetadata[] getBaseParamMetadatas();

    public void setBaseParamMetadatas(ResourceParamMetadata ... baseParamMetadatas);

    public Map<Class<? extends T>, U> getBeanMetadatas();

    public Map<String, U> getMetadatas();
}
