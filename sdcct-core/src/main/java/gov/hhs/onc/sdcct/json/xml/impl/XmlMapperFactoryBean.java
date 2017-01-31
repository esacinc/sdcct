package gov.hhs.onc.sdcct.json.xml.impl;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.ser.XmlBeanSerializerModifier;
import gov.hhs.onc.sdcct.json.impl.SdcctModule;
import gov.hhs.onc.sdcct.json.impl.SdcctObjectMapperFactoryBean;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlInputFactory;
import gov.hhs.onc.sdcct.xml.impl.SdcctXmlOutputFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlMapperFactoryBean extends SdcctObjectMapperFactoryBean {
    private static class SdcctXmlModule extends SdcctModule {
        private final static long serialVersionUID = 0L;

        @Override
        public void setupModule(SetupContext context) {
            context.appendAnnotationIntrospector(new JacksonXmlAnnotationIntrospector(false));

            context.addBeanSerializerModifier(new XmlBeanSerializerModifier());

            super.setupModule(context);
        }
    }

    @Autowired
    private SdcctXmlOutputFactory xmlOutFactory;

    @Autowired
    private SdcctXmlInputFactory xmlInFactory;

    @Override
    public void afterPropertiesSet() {
        SdcctXmlMapper mapper = new SdcctXmlMapper(this.xmlInFactory, this.xmlOutFactory);
        this.setObjectMapper(mapper);

        super.afterPropertiesSet();

        mapper.registerModule(new SdcctXmlModule());
    }

    @Override
    public SdcctXmlMapper getObject() {
        return ((SdcctXmlMapper) super.getObject());
    }

    @Override
    public Class<?> getObjectType() {
        return SdcctXmlMapper.class;
    }
}
