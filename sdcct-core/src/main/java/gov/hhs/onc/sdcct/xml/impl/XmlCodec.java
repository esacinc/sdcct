package gov.hhs.onc.sdcct.xml.impl;

import gov.hhs.onc.sdcct.io.impl.ByteArrayResult;
import gov.hhs.onc.sdcct.io.impl.ByteArraySource;
import gov.hhs.onc.sdcct.transform.content.ContentEncodeOptions;
import gov.hhs.onc.sdcct.transform.content.SdcctContentType;
import gov.hhs.onc.sdcct.transform.content.impl.AbstractContentCodec;
import gov.hhs.onc.sdcct.transform.impl.SdcctSerializer;
import gov.hhs.onc.sdcct.utils.SdcctOptionUtils;
import gov.hhs.onc.sdcct.utils.SdcctStreamUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.jaxb.JAXBContextCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;

public class XmlCodec extends AbstractContentCodec implements InitializingBean {
    private final static String CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX = "create";

    private final static Logger LOGGER = LoggerFactory.getLogger(XmlCodec.class);

    @Resource(name = "serializerXml")
    private SdcctSerializer serializer;

    @Resource(name = "serializerXmlPretty")
    private SdcctSerializer prettySerializer;

    private Map<String, Object> contextProps = new HashMap<>();
    private Map<String, Object> marshallerProps = new HashMap<>();
    private Object[][] objFactories;
    private Map<String, Object> unmarshallerProps = new HashMap<>();
    private Map<Class<?>, JAXBContext> contextClasses = new LinkedHashMap<>();
    private Map<Class<?>, Object> contextClassObjFactories = new LinkedHashMap<>();
    private Map<Class<?>, Method> contextClassCreateElemMethods = new LinkedHashMap<>();

    public XmlCodec() {
        super(SdcctContentType.XML);
    }

    @Override
    public <T> T decode(byte[] src, Class<T> resultClass, Map<String, Object> opts) throws Exception {
        return this.decode(new ByteArraySource(src), resultClass, opts);
    }

    public <T> T decode(Source src, Class<T> resultClass) throws Exception {
        return this.decode(src, resultClass, Collections.emptyMap());
    }

    public <T> T decode(Source src, Class<T> resultClass, Map<String, Object> opts) throws Exception {
        if (!this.contextClasses.containsKey(resultClass)) {
            throw new JAXBException(String.format("Unable to determine JAXB context for result class (name=%s).", resultClass.getName()));
        }

        Unmarshaller unmarshaller = this.contextClasses.get(resultClass).createUnmarshaller();
        Object unmarshallerPropValue;

        for (String unmarshallerPropName : this.unmarshallerProps.keySet()) {
            unmarshallerPropValue = this.unmarshallerProps.get(unmarshallerPropName);

            try {
                unmarshaller.setProperty(unmarshallerPropName, unmarshallerPropValue);
            } catch (PropertyException e) {
                throw new JAXBException(
                    String.format("Unable to set JAXB unmarshaller property (name=%s) value: %s", unmarshallerPropName, unmarshallerPropValue), e);
            }
        }

        Object result = unmarshaller.unmarshal(src);

        return resultClass.cast(((result instanceof JAXBElement<?>) ? ((JAXBElement<?>) result).getValue() : result));
    }

    public <T extends Result> T decode(Source src, T result) throws Exception {
        return this.decode(src, result, Collections.emptyMap());
    }

    public <T extends Result> T decode(Source src, T result, Map<String, Object> opts) throws Exception {
        return this.serializer.serializeToResult(src, result);
    }

    @Override
    public byte[] encode(Object src, Map<String, Object> opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Object src, T result) throws Exception {
        return this.encode(src, result, Collections.emptyMap());
    }

    public <T extends Result> T encode(Object src, T result, Map<String, Object> opts) throws Exception {
        Class<?> srcClass = src.getClass();

        if (!this.contextClasses.containsKey(srcClass)) {
            throw new JAXBException(String.format("Unable to determine JAXB context for source class (name=%s).", srcClass.getName()));
        }

        if ((AnnotationUtils.findAnnotation(srcClass, XmlRootElement.class) == null) && this.contextClassCreateElemMethods.containsKey(srcClass)) {
            Method srcClassCreateElemMethod = this.contextClassCreateElemMethods.get(srcClass);
            Object srcClassObjFactory = this.contextClassObjFactories.get(srcClass);

            try {
                src = srcClassCreateElemMethod.invoke(srcClassObjFactory, src);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new JAXBException(
                    String.format("Unable to invoke JAXB object factory (class=%s) element creation method (name=%s) for source class (name=%s).",
                        srcClassObjFactory.getClass().getName(), srcClassCreateElemMethod.getName(), srcClass.getName()),
                    e);
            }
        }

        Marshaller marshaller = this.contextClasses.get(srcClass).createMarshaller();
        Object marshallerPropValue;

        for (String marshallerPropName : this.marshallerProps.keySet()) {
            marshallerPropValue = this.marshallerProps.get(marshallerPropName);

            try {
                marshaller.setProperty(marshallerPropName, marshallerPropValue);
            } catch (PropertyException e) {
                throw new JAXBException(String.format("Unable to set JAXB marshaller property (name=%s) value: %s", marshallerPropName, marshallerPropValue),
                    e);
            }
        }

        marshaller.marshal(src, this.buildSerializer(opts, this.defaultEncodeOpts).getContentHandler(result, null, null));

        return result;
    }

    public byte[] encode(Source src) throws Exception {
        return this.encode(src, Collections.emptyMap());
    }

    public byte[] encode(Source src, Map<String, Object> opts) throws Exception {
        return this.encode(src, new ByteArrayResult(), opts).getBytes();
    }

    public <T extends Result> T encode(Source src, T result) throws Exception {
        return this.encode(src, result, Collections.emptyMap());
    }

    public <T extends Result> T encode(Source src, T result, Map<String, Object> opts) throws Exception {
        return this.buildSerializer(opts, this.defaultEncodeOpts).serializeToResult(src, result);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<Object, Class<?>> contextObjFactoryClasses;
        JAXBContext context;
        int numContextObjFactoryMethodParams;
        Class<?> contextClass;

        for (Object[] contextObjFactories : this.objFactories) {
            context = JAXBContextCache.getCachedContextAndSchemas(
                new LinkedHashSet<>((contextObjFactoryClasses = Stream.of(contextObjFactories)
                    .collect(SdcctStreamUtils.toMap(Function.identity(), Object::getClass, () -> new LinkedHashMap<>(contextObjFactories.length)))).values()),
                null, this.contextProps, null, true).getContext();

            for (Object contextObjFactory : contextObjFactoryClasses.keySet()) {
                for (Method contextObjFactoryMethod : contextObjFactoryClasses.get(contextObjFactory).getMethods()) {
                    if (!StringUtils.startsWith(contextObjFactoryMethod.getName(), CREATE_OBJ_FACTORY_METHOD_NAME_PREFIX)) {
                        continue;
                    }

                    if ((numContextObjFactoryMethodParams = contextObjFactoryMethod.getParameterCount()) == 0) {
                        this.contextClasses.put((contextClass = contextObjFactoryMethod.getReturnType()), context);

                        this.contextClassObjFactories.put(contextClass, contextObjFactory);
                    } else if ((numContextObjFactoryMethodParams == 1) && contextObjFactoryMethod.isAnnotationPresent(XmlElementDecl.class)) {
                        this.contextClassCreateElemMethods.put(contextObjFactoryMethod.getParameterTypes()[0], contextObjFactoryMethod);
                    }
                }
            }

            LOGGER.info(String.format("Built JAXB context (numClasses=%d, numClassCreateElemMethods=%d) for object factory class(es): [%s]",
                this.contextClasses.size(), this.contextClassCreateElemMethods.size(),
                contextObjFactoryClasses.values().stream().map(Class::getName).collect(Collectors.joining(", "))));
        }
    }

    private SdcctSerializer buildSerializer(Map<String, Object> opts, Map<String, Object> defaultOpts) {
        return (SdcctOptionUtils.getBooleanValue(opts, ContentEncodeOptions.PRETTY_NAME, defaultOpts) ? this.prettySerializer : this.serializer);
    }

    public Map<String, Object> getContextProperties() {
        return this.contextProps;
    }

    public void setContextProperties(Map<String, Object> contextProps) {
        this.contextProps.clear();
        this.contextProps.putAll(contextProps);
    }

    public Map<String, Object> getMarshallerProperties() {
        return this.marshallerProps;
    }

    public void setMarshallerProperties(Map<String, Object> marshallerProps) {
        this.marshallerProps.clear();
        this.marshallerProps.putAll(marshallerProps);
    }

    public Object[][] getObjectFactories() {
        return this.objFactories;
    }

    public void setObjectFactories(Object[] ... objFactories) {
        this.objFactories = objFactories;
    }

    public Map<String, Object> getUnmarshallerProperties() {
        return this.unmarshallerProps;
    }

    public void setUnmarshallerProperties(Map<String, Object> unmarshallerProps) {
        this.unmarshallerProps.clear();
        this.unmarshallerProps.putAll(unmarshallerProps);
    }
}
