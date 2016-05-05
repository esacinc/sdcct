package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import java.util.Map;

public interface ResourceMetadata<T extends Enum<T> & IdentifiedBean, U> extends ResourceMetadataComponent {
    public Class<U> getBeanClass();

    public Class<? extends U> getBeanImplClass();

    public void addSearchParamMetadatas(SearchParamMetadata ... searchParamMetadatas);

    public Map<String, SearchParamMetadata> getSearchParamMetadatas();

    public T getType();
}
