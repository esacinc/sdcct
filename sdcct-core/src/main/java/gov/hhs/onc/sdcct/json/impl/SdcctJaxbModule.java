package gov.hhs.onc.sdcct.json.impl;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.jaxb.XmlJaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import gov.hhs.onc.sdcct.utils.SdcctClassUtils;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.jaxb.metadata.JaxbElementMetadata;
import javax.annotation.Nullable;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlSchema;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("moduleJaxb")
public class SdcctJaxbModule extends JaxbAnnotationModule implements BeanClassLoaderAware {
    public static interface JaxbElementMixIn {
        @JsonValue
        public Object getValue();
    }

    private class SdcctJaxbAnnotationIntrospector extends XmlJaxbAnnotationIntrospector {
        private final static long serialVersionUID = 0L;

        public SdcctJaxbAnnotationIntrospector(TypeFactory typeFactory) {
            super(typeFactory);
        }

        @Nullable
        @Override
        public PropertyName findRootName(AnnotatedClass annotatedClass) {
            Class<?> clazz = annotatedClass.getRawType();

            if (!SdcctClassUtils.isImplClass(clazz)) {
                try {
                    clazz = SdcctClassUtils.buildImplClass(SdcctJaxbModule.this.beanClassLoader, Object.class, clazz);
                } catch (ClassNotFoundException ignored) {
                    return super.findRootName(annotatedClass);
                }
            }

            try {
                JaxbElementMetadata<?> jaxbElemMetadata =
                    SdcctJaxbModule.this.jaxbContextRepo.findElementMetadata(SdcctJaxbModule.this.jaxbContextRepo.findTypeMetadata(clazz));

                return ((jaxbElemMetadata != null)
                    ? new PropertyName(jaxbElemMetadata.getName(), jaxbElemMetadata.getSchema().getName()) : super.findRootName(annotatedClass));
            } catch (JAXBException ignored) {
                return super.findRootName(annotatedClass);
            }
        }

        @Nullable
        @Override
        public String findNamespace(Annotated annotatedObj) {
            String nsUri = super.findNamespace(annotatedObj);

            if (nsUri != null) {
                return nsUri;
            }

            Package implPkg = SdcctClassUtils.buildImplPackage(annotatedObj.getRawType().getPackage());

            return (((implPkg != null) && implPkg.isAnnotationPresent(XmlSchema.class)) ? implPkg.getAnnotation(XmlSchema.class).namespace() : null);
        }
    }

    @Autowired
    @Lazy
    private JaxbContextRepository jaxbContextRepo;

    private ClassLoader beanClassLoader;

    @Override
    public void setupModule(SetupContext context) {
        (this._introspector = new SdcctJaxbAnnotationIntrospector(context.getTypeFactory()))
            .setNonNillableInclusion((this._nonNillableInclusion = Include.NON_DEFAULT));
        this._priority = Priority.SECONDARY;

        super.setupModule(context);

        context.setMixInAnnotations(JAXBElement.class, JaxbElementMixIn.class);
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }
}
