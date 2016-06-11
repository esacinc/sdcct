package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.beans.PathBean;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbComplexTypeMetadata;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathExecutable;
import java.util.Map;

public interface ResourceMetadata<T> extends PathBean, ResourceMetadataComponent {
    public Class<T> getBeanClass();

    public Class<? extends T> getBeanImplClass();

    public SdcctXpathExecutable[] getCanonicalRemoveXpathExecutables();

    public void setCanonicalRemoveXpathExecutables(SdcctXpathExecutable ... canonicalRemoveXpathExecs);

    public JaxbComplexTypeMetadata<?> getJaxbTypeMetadata();

    public void setJaxbTypeMetadata(JaxbComplexTypeMetadata<?> jaxbTypeMetadata);

    public void addParamMetadatas(ResourceParamMetadata ... paramMetadatas);

    public Map<String, ResourceParamMetadata> getParamMetadatas();

    public void setParamMetadatas(ResourceParamMetadata ... paramMetadatas);
}
