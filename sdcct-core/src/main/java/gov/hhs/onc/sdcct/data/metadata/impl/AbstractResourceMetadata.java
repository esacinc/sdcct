package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import java.net.URI;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public abstract class AbstractResourceMetadata<T extends Enum<T> & IdentifiedBean, U> extends AbstractResourceMetadataComponent implements
    ResourceMetadata<T, U> {
    protected T type;
    protected Class<U> beanClass;
    protected Class<? extends U> beanImplClass;
    protected Map<String, SearchParamMetadata> searchParamMetadatas = new TreeMap<>();

    protected AbstractResourceMetadata(SpecificationType specType, String id, String name, @Nullable URI uri, T type, Class<U> beanClass,
        Class<? extends U> beanImplClass) {
        super(specType, id, name, uri);

        this.type = type;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public Class<U> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends U> getBeanImplClass() {
        return this.beanImplClass;
    }

    @Override
    public void addSearchParamMetadatas(SearchParamMetadata ... searchParamMetadatas) {
        Stream.of(searchParamMetadatas).forEach(searchParamMetadata -> this.searchParamMetadatas.put(searchParamMetadata.getName(), searchParamMetadata));
    }

    @Override
    public Map<String, SearchParamMetadata> getSearchParamMetadatas() {
        return this.searchParamMetadatas;
    }

    @Override
    public T getType() {
        return this.type;
    }
}
