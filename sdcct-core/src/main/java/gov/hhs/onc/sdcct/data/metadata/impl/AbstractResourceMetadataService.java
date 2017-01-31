package gov.hhs.onc.sdcct.data.metadata.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.jaxb.JaxbContextRepository;
import gov.hhs.onc.sdcct.xml.xpath.saxon.impl.SdcctXpathCompiler;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResourceMetadataService<T, U extends ResourceMetadata<?>> implements ResourceMetadataService<T, U> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected List<U> metadataItems;

    @Autowired
    protected JaxbContextRepository jaxbContextRepo;

    @Autowired
    protected SdcctXpathCompiler xpathCompiler;

    @Autowired
    protected XmlCodec xmlCodec;

    protected SpecificationType specType;
    protected ResourceParamMetadata[] baseParamMetadatas;
    protected Map<String, U> metadatas = new TreeMap<>();
    protected Map<Class<? extends T>, U> beanMetadatas = new TreeMap<>(Comparator.comparing(Class::getName));

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractResourceMetadataService.class);

    protected AbstractResourceMetadataService(SpecificationType specType) {
        this.specType = specType;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() throws Exception {
        String id, name, path;

        for (U metadata : this.metadataItems) {
            id = metadata.getId();
            name = metadata.getName();
            path = metadata.getPath();

            try {
                this.metadatas.put(path, this.buildMetadata(id, path, metadata));
                this.beanMetadatas.put(((Class<? extends T>) metadata.getBeanImplClass()), metadata);

                LOGGER.debug(String.format("Built %s resource type (id=%s, name=%s, path=%s) metadata (params=[%s]).", this.specType, id, name, path,
                    metadata.getParamMetadatas().values().stream().map(NamedBean::getName).collect(Collectors.joining(", "))));
            } catch (Exception e) {
                throw new FatalBeanException(
                    String.format("Unable to build %s resource type (id=%s, name=%s, path=%s) metadata.", this.specType, id, name, path), e);
            }
        }
    }

    protected U buildMetadata(String id, String path, U metadata) throws Exception {
        metadata.addParamMetadatas(this.baseParamMetadatas);

        return metadata;
    }

    @Override
    public ResourceParamMetadata[] getBaseParamMetadatas() {
        return this.baseParamMetadatas;
    }

    @Override
    public void setBaseParamMetadatas(ResourceParamMetadata ... baseParamMetadatas) {
        this.baseParamMetadatas = baseParamMetadatas;
    }

    @Override
    public Map<Class<? extends T>, U> getBeanMetadatas() {
        return this.beanMetadatas;
    }

    @Override
    public Map<String, U> getMetadatas() {
        return this.metadatas;
    }

    @Override
    public SpecificationType getSpecificationType() {
        return this.specType;
    }
}
