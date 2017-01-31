package gov.hhs.onc.sdcct.xml.jaxb.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbContextMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbElementMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbSchemaMetadata;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbTypeMetadata;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.collections4.MapUtils;

public class JaxbContextRepositoryImpl implements JaxbContextRepository {
    private Map<String, JaxbContextMetadata> contextMetadatas;
    private Map<String, Object> defaultMarshallerProps = new HashMap<>();
    private Map<String, Object> defaultUnmarshallerProps = new HashMap<>();

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> Object buildSource(JaxbTypeMetadata<?, ?> srcTypeMetadata, T src) throws JAXBException {
        JaxbElementMetadata<?> srcElemMetadata = this.findElementMetadata(srcTypeMetadata);

        // noinspection ConstantConditions
        return ((srcElemMetadata != null)
            ? new JAXBElement<>(srcElemMetadata.getQname(), ((Class<T>) srcElemMetadata.getType().getBeanImplClass()), srcElemMetadata.getScope(), src) : src);
    }

    @Override
    public <T> Marshaller buildMarshaller(T src, @Nullable Map<String, Object> marshallerProps) throws JAXBException {
        Marshaller marshaller = this.findTypeMetadata(src.getClass()).getContext().getContext().createMarshaller();
        Map<String, Object> mergedMarshallerProps = new HashMap<>(this.defaultMarshallerProps);

        if (!MapUtils.isEmpty(marshallerProps)) {
            mergedMarshallerProps.putAll(marshallerProps);
        }

        Object marshallerPropValue;

        for (String marshallerPropName : mergedMarshallerProps.keySet()) {
            marshallerPropValue = mergedMarshallerProps.get(marshallerPropName);

            try {
                marshaller.setProperty(marshallerPropName, marshallerPropValue);
            } catch (PropertyException e) {
                throw new JAXBException(String.format("Unable to set JAXB marshaller property (name=%s) value: %s", marshallerPropName, marshallerPropValue),
                    e);
            }
        }

        return marshaller;
    }

    @Override
    public <T> Unmarshaller buildUnmarshaller(Class<T> resultClass, @Nullable Map<String, Object> unmarshallerProps) throws JAXBException {
        Unmarshaller unmarshaller = this.findTypeMetadata(resultClass).getContext().getContext().createUnmarshaller();
        Map<String, Object> mergedUnmarshallerProps = new HashMap<>(this.defaultUnmarshallerProps);

        if (!MapUtils.isEmpty(unmarshallerProps)) {
            mergedUnmarshallerProps.putAll(unmarshallerProps);
        }

        Object unmarshallerPropValue;

        for (String unmarshallerPropName : mergedUnmarshallerProps.keySet()) {
            unmarshallerPropValue = mergedUnmarshallerProps.get(unmarshallerPropName);

            try {
                unmarshaller.setProperty(unmarshallerPropName, unmarshallerPropValue);
            } catch (PropertyException e) {
                throw new JAXBException(
                    String.format("Unable to set JAXB unmarshaller property (name=%s) value: %s", unmarshallerPropName, unmarshallerPropValue), e);
            }
        }

        return unmarshaller;
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> JaxbElementMetadata<T> findElementMetadata(JaxbTypeMetadata<?, ?> typeMetadata) {
        String typeName = typeMetadata.getName();
        Map<String, String> schemaElemTypeNames;

        for (JaxbSchemaMetadata schemaMetadata : typeMetadata.getContext().getSchemas().values()) {
            if ((schemaElemTypeNames = schemaMetadata.getElementTypeNames()).containsKey(typeName)) {
                return ((JaxbElementMetadata<T>) schemaMetadata.getElementNames().get(schemaElemTypeNames.get(typeName)));
            }
        }

        return null;
    }

    @Override
    public JaxbTypeMetadata<?, ?> findTypeMetadata(Class<?> beanImplClass) throws JAXBException {
        Map<Class<?>, JaxbTypeMetadata<?, ?>> schemaTypeBeanClassMetadatas;

        for (JaxbContextMetadata contextMetadata : this.contextMetadatas.values()) {
            for (JaxbSchemaMetadata schemaMetadata : contextMetadata.getSchemas().values()) {
                if ((schemaTypeBeanClassMetadatas = schemaMetadata.getTypeBeanClasses()).containsKey(beanImplClass)) {
                    return schemaTypeBeanClassMetadatas.get(beanImplClass);
                }
            }
        }

        throw new JAXBException(String.format("Unable to find JAXB type (beanImplClass=%s) metadata.", beanImplClass.getName()));
    }

    @Override
    public JaxbContextMetadata findContextMetadata(String schemaNsUri) throws JAXBException {
        JaxbSchemaMetadata schemaMetadata = this.findSchemaMetadata(schemaNsUri);

        if (schemaMetadata == null) {
            throw new JAXBException(String.format("Unable to find JAXB schema (nsUri=%s) context metadata.", schemaNsUri));
        }

        return schemaMetadata.getContext();
    }

    @Nullable
    @Override
    public JaxbSchemaMetadata findSchemaMetadata(String schemaNsUri) {
        Map<String, JaxbSchemaMetadata> schemaMetadatas;

        for (JaxbContextMetadata contextMetadata : this.contextMetadatas.values()) {
            if ((schemaMetadatas = contextMetadata.getSchemas()).containsKey(schemaNsUri)) {
                return schemaMetadatas.get(schemaNsUri);
            }
        }

        return null;
    }

    @Override
    public Map<String, JaxbContextMetadata> getContextMetadatas() {
        return this.contextMetadatas;
    }

    @Override
    public void setContextMetadatas(JaxbContextMetadata ... contextMetadatas) {
        this.contextMetadatas = Stream.of(contextMetadatas).collect(SdcctStreamUtils.toMap(NamedBean::getName, Function.identity(), LinkedHashMap::new));
    }

    @Override
    public Map<String, Object> getDefaultMarshallerProperties() {
        return this.defaultMarshallerProps;
    }

    @Override
    public void setDefaultMarshallerProperties(Map<String, Object> defaultMarshallerProps) {
        this.defaultMarshallerProps.clear();
        this.defaultMarshallerProps.putAll(defaultMarshallerProps);
    }

    @Override
    public Map<String, Object> getDefaultUnmarshallerProperties() {
        return this.defaultUnmarshallerProps;
    }

    @Override
    public void setDefaultUnmarshallerProperties(Map<String, Object> defaultUnmarshallerProps) {
        this.defaultUnmarshallerProps.clear();
        this.defaultUnmarshallerProps.putAll(defaultUnmarshallerProps);
    }
}
