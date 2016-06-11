package gov.hhs.onc.sdcct.xml.jaxb;

import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import java.util.Map;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public interface JaxbContextRepository {
    public <T> Object buildSource(JaxbTypeMetadata<?, ?> srcTypeMetadata, T src) throws JAXBException;

    public <T> Marshaller buildMarshaller(T src, @Nullable Map<String, Object> marshallerProps) throws JAXBException;

    public <T> Unmarshaller buildUnmarshaller(Class<T> resultClass, @Nullable Map<String, Object> unmarshallerProps) throws JAXBException;

    public JaxbTypeMetadata<?, ?> findTypeMetadata(Class<?> beanImplClass) throws JAXBException;

    public Map<String, JaxbContextMetadata> getContextMetadatas();

    public void setContextMetadatas(JaxbContextMetadata ... contextMetadatas);

    public Map<String, Object> getDefaultMarshallerProperties();

    public void setDefaultMarshallerProperties(Map<String, Object> defaultMarshallerProps);

    public Map<String, Object> getDefaultUnmarshallerProperties();

    public void setDefaultUnmarshallerProperties(Map<String, Object> defaultUnmarshallerProps);
}
