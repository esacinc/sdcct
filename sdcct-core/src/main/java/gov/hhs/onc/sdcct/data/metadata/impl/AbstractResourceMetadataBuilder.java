package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.beans.SpecificationType;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataBuilder;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xpath.impl.SdcctXpathCompiler;
import java.util.Map;
import java.util.stream.Collectors;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResourceMetadataBuilder<T extends Enum<T> & IdentifiedBean, U, V extends ResourceMetadata<T, ? extends U>> implements
    ResourceMetadataBuilder<T, U, V> {
    @Autowired
    protected SdcctXpathCompiler xpathCompiler;

    @Autowired
    protected XmlCodec xmlCodec;

    protected SpecificationType specType;
    protected ClassLoader beanClassLoader;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractResourceMetadataBuilder.class);

    protected AbstractResourceMetadataBuilder(SpecificationType specType) {
        this.specType = specType;
    }

    @Override
    public Map<T, V> build() {
        try {
            Map<T, V> resources = this.buildInternal();

            LOGGER.debug(String.format("Built metadata for %d %s resource(s): [%s]", resources.size(), this.specType.name(),
                resources.values().stream().map(NamedBean::getName).collect(Collectors.joining(", "))));

            return resources;
        } catch (Exception e) {
            throw new HibernateException("Unable to build metadata for resource(s).", e);
        }
    }

    protected abstract Map<T, V> buildInternal() throws Exception;

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
