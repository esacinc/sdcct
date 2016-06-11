package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public abstract class AbstractResourceMetadata<T> extends AbstractResourceMetadataComponent implements ResourceMetadata<T> {
    protected String path;
    protected Class<T> beanClass;
    protected Class<? extends T> beanImplClass;
    protected SdcctXpathExecutable[] canonicalRemoveXpathExecs;
    protected JaxbComplexTypeMetadata<?> jaxbTypeMetadata;
    protected Map<String, ResourceParamMetadata> paramMetadatas = new TreeMap<>();

    protected AbstractResourceMetadata(SpecificationType specType, String id, String name, String path, Class<T> beanClass, Class<? extends T> beanImplClass) {
        super(specType, id, name);

        this.path = path;
        this.beanClass = beanClass;
        this.beanImplClass = beanImplClass;
    }

    @Override
    public Class<T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public Class<? extends T> getBeanImplClass() {
        return this.beanImplClass;
    }

    @Override
    public SdcctXpathExecutable[] getCanonicalRemoveXpathExecutables() {
        return this.canonicalRemoveXpathExecs;
    }

    @Override
    public void setCanonicalRemoveXpathExecutables(SdcctXpathExecutable ... canonicalRemoveXpathExecs) {
        this.canonicalRemoveXpathExecs = canonicalRemoveXpathExecs;
    }

    @Override
    public JaxbComplexTypeMetadata<?> getJaxbTypeMetadata() {
        return this.jaxbTypeMetadata;
    }

    @Override
    public void setJaxbTypeMetadata(JaxbComplexTypeMetadata<?> jaxbTypeMetadata) {
        this.jaxbTypeMetadata = jaxbTypeMetadata;
    }

    @Override
    public void addParamMetadatas(ResourceParamMetadata ... paramMetadatas) {
        Stream.of(paramMetadatas).forEach(paramMetadata -> this.paramMetadatas.put(paramMetadata.getName(), paramMetadata));
    }

    @Override
    public Map<String, ResourceParamMetadata> getParamMetadatas() {
        return this.paramMetadatas;
    }

    @Override
    public void setParamMetadatas(ResourceParamMetadata ... paramMetadatas) {
        this.paramMetadatas.clear();

        this.addParamMetadatas(paramMetadatas);
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
