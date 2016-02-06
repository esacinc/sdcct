package gov.hhs.onc.sdcct.xml.jaxb.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextId;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextInfo;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbElementInfo;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.jaxb.JAXBContextCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

public class JaxbContextRepositoryImpl implements JaxbContextRepository {
    private final static String CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX = "create";

    private final static Logger LOGGER = LoggerFactory.getLogger(JaxbContextRepositoryImpl.class);

    private Map<JaxbContextId, JaxbContextInfo> contextInfos;
    private Map<String, Object> defaultMarshallerProps = new HashMap<>();
    private Map<String, Object> defaultUnmarshallerProps = new HashMap<>();

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public <T> JaxbSource<T> buildSource(T src, @Nullable Map<String, Object> marshallerProps) throws JAXBException {
        Class<?> srcClass = src.getClass();
        JaxbContextInfo contextInfo = this.findContextInfo(srcClass);

        Marshaller marshaller = contextInfo.getContext().createMarshaller();
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

        if (AnnotationUtils.findAnnotation(srcClass, XmlRootElement.class) == null) {
            JaxbElementInfo<?> srcElemInfo = contextInfo.getElementInfos().get(srcClass);

            if (srcElemInfo != null) {
                return new JaxbSource<>(marshaller,
                    new JAXBElement<>(srcElemInfo.getName(), ((Class<T>) srcElemInfo.getDeclaredType()), srcElemInfo.getScope(), src));
            }
        }

        return new JaxbSource<>(marshaller, src);
    }

    @Override
    public <T> JaxbResult<T> buildResult(Class<T> resultClass, @Nullable Map<String, Object> unmarshallerProps) throws JAXBException {
        Unmarshaller unmarshaller = this.findContextInfo(resultClass).getContext().createUnmarshaller();
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

        return new JaxbResult<>(unmarshaller, resultClass);
    }

    @Override
    public JaxbContextInfo findContextInfo(Class<?> clazz) throws JAXBException {
        return this.contextInfos.values().stream().filter(contextInfo -> contextInfo.getClasses().contains(clazz)).findFirst()
            .orElseThrow(() -> new JAXBException(String.format("Unable to find JAXB context for object class (name=%s).", clazz.getName())));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JaxbContextInfo contextInfo;
        Object[] contextObjFactories;
        Map<Object, Class<?>> contextObjFactoryClasses;
        Set<Class<?>> contextClasses;
        Map<Class<?>, JaxbElementInfo<?>> contextElemInfos;
        int numContextObjFactoryMethodParams;
        XmlElementDecl contextElemDeclAnno;
        Class<?> contextElemDeclClass, contextElemDeclScopeClass;

        for (JaxbContextId contextId : this.contextInfos.keySet()) {
            (contextInfo = this.contextInfos.get(contextId)).setContext(JAXBContextCache.getCachedContextAndSchemas(
                new LinkedHashSet<>((contextObjFactoryClasses = Stream.of((contextObjFactories = contextInfo.getObjectFactories()))
                    .collect(SdcctStreamUtils.toMap(Function.identity(), Object::getClass, LinkedHashMap::new))).values()),
                null, contextInfo.getContextProperties(), null, true).getContext());

            contextClasses = contextInfo.getClasses();
            contextElemInfos = contextInfo.getElementInfos();

            for (Object contextObjFactory : contextObjFactories) {
                for (Method contextObjFactoryMethod : contextObjFactoryClasses.get(contextObjFactory).getDeclaredMethods()) {
                    if (!StringUtils.startsWith(contextObjFactoryMethod.getName(), CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX)) {
                        continue;
                    }

                    if ((numContextObjFactoryMethodParams = contextObjFactoryMethod.getParameterCount()) == 0) {
                        contextClasses.add(contextObjFactoryMethod.getReturnType());
                    } else if ((numContextObjFactoryMethodParams == 1)
                        && ((contextElemDeclAnno = contextObjFactoryMethod.getAnnotation(XmlElementDecl.class)) != null)) {
                        contextElemInfos.put((contextElemDeclClass = contextObjFactoryMethod.getParameterTypes()[0]),
                            new JaxbElementInfoImpl<>(contextElemDeclClass, new QName(contextElemDeclAnno.namespace(), contextElemDeclAnno.name()),
                                ((contextElemDeclScopeClass = contextElemDeclAnno.scope()).equals(XmlElementDecl.GLOBAL.class)
                                    ? null : contextElemDeclScopeClass)));
                    }
                }
            }

            LOGGER.info(String.format("Built JAXB context (id=%s) info (numClasses=%d, numElemInfos=%d) for object factory class(es): [%s]", contextId.getId(),
                contextClasses.size(), contextElemInfos.size(),
                contextObjFactoryClasses.values().stream().map(Class::getName).collect(Collectors.joining(", "))));
        }
    }

    @Override
    public Map<JaxbContextId, JaxbContextInfo> getContextInfos() {
        return this.contextInfos;
    }

    @Override
    public void setContextInfos(JaxbContextInfo ... contextInfos) {
        this.contextInfos = Stream.of(contextInfos).collect(SdcctStreamUtils.toMap(JaxbContextInfo::getId, Function.identity(), LinkedHashMap::new));
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
