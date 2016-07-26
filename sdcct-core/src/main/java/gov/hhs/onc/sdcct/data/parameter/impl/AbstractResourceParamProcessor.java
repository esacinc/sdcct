package gov.hhs.onc.sdcct.data.parameter.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.impl.AbstractSdcctResourceAccessor;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadataService;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.parameter.DateResourceParam;
import gov.hhs.onc.sdcct.data.parameter.NumberResourceParam;
import gov.hhs.onc.sdcct.data.parameter.QuantityResourceParam;
import gov.hhs.onc.sdcct.data.parameter.RefResourceParam;
import gov.hhs.onc.sdcct.data.parameter.ResourceParam;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamProcessor;
import gov.hhs.onc.sdcct.data.parameter.StringResourceParam;
import gov.hhs.onc.sdcct.data.parameter.TermResourceParam;
import gov.hhs.onc.sdcct.data.parameter.TokenResourceParam;
import gov.hhs.onc.sdcct.data.parameter.UriResourceParam;
import gov.hhs.onc.sdcct.transform.content.path.ContentPathBuilder;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptionsImpl;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.tree.linked.ElementImpl;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResourceParamProcessor<T, U extends ResourceMetadata<?>, V extends SdcctResource> extends AbstractSdcctResourceAccessor<T, U, V>
    implements ResourceParamProcessor<T, U, V> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected ResourceMetadataService<T, U> resourceMetadataService;

    @Autowired
    protected XmlCodec xmlCodec;

    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    protected ContentPathBuilder contentPathBuilder;
    protected Map<String, U> resourceMetadatas;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractResourceParamProcessor.class);

    protected AbstractResourceParamProcessor(SpecificationType specType, Class<T> beanClass, Class<? extends T> beanImplClass, Class<V> entityClass,
        Class<? extends V> entityImplClass) {
        super(specType, beanClass, beanImplClass, entityClass, entityImplClass);
    }

    @Override
    public void process(XdmDocument contentDoc, V entity) throws HibernateException {
        String type = entity.getType();

        if (!this.resourceMetadatas.containsKey(type)) {
            throw new HibernateException(String.format("Unknown resource entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) type (%s).",
                entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), type));
        }

        U resourceMetadata = this.resourceMetadatas.get(type);

        try {
            this.processResourceParams(type, resourceMetadata, contentDoc, entity);
        } catch (Exception e) {
            throw new HibernateException(
                String.format("Unable to process resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) parameters.", type,
                    entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion()),
                e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.resourceMetadatas = this.resourceMetadataService.getMetadatas();
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected static Map<MultiKey<Serializable>, ResourceParam<?>> mapExistingResourceParams(SdcctResource entity) {
        Map<MultiKey<Serializable>, ResourceParam<?>> existingResourceParams = new LinkedHashMap<>();
        Builder<Serializable> existingResourceParamKeyStreamBuilder;
        TermResourceParam<?> existingTermResourceParam;

        for (ResourceParam<?> existingResourceParam : IteratorUtils.asIterable(iterateExistingResourceParams(entity))) {
            if (existingResourceParam.isMeta()) {
                continue;
            }

            (existingResourceParamKeyStreamBuilder = Stream.builder()).add(existingResourceParam.getName());

            if (existingResourceParam instanceof TermResourceParam) {
                existingResourceParamKeyStreamBuilder.add((existingTermResourceParam = ((TermResourceParam<?>) existingResourceParam)).getCodeSystemUri());
                existingResourceParamKeyStreamBuilder.add(existingTermResourceParam.getCodeSystemVersion());

                if (existingResourceParam instanceof QuantityResourceParam) {
                    existingResourceParamKeyStreamBuilder.add(((QuantityResourceParam) existingTermResourceParam).getUnits());
                }

                existingResourceParamKeyStreamBuilder.add(existingTermResourceParam.getDisplayName());
            }

            existingResourceParamKeyStreamBuilder.add(existingResourceParam.getValue());

            existingResourceParams.put(new MultiKey<>(existingResourceParamKeyStreamBuilder.build().toArray(Serializable[]::new)), existingResourceParam);
        }

        return existingResourceParams;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected static Iterator<ResourceParam<?>> iterateExistingResourceParams(SdcctResource entity) {
        return IteratorUtils.chainedIterator(entity.getDateParams().iterator(), entity.getNumberParams().iterator(), entity.getQuantityParams().iterator(),
            entity.getRefParams().iterator(), entity.getStringParams().iterator(), entity.getTokenParams().iterator(), entity.getUriParams().iterator());
    }

    protected void processResourceParams(String type, U resourceMetadata, XdmDocument contentDoc, V entity) throws Exception {
        ElementImpl contentDocElem = contentDoc.getDocumentElement();
        Map<String, ResourceParamMetadata> resourceParamMetadatas = resourceMetadata.getParamMetadatas();
        Map<MultiKey<Serializable>, ResourceParam<?>> existingResourceParams = mapExistingResourceParams(entity), resourceParams = new LinkedHashMap<>();
        ResourceParamMetadata resourceParamMetadata;
        List<XdmItem> resourceParamItems;

        for (String resourceParamName : resourceParamMetadatas.keySet()) {
            // noinspection ConstantConditions
            if ((resourceParamMetadata = resourceParamMetadatas.get(resourceParamName)).isInline() || !resourceParamMetadata.hasXpathExecutable() ||
                (resourceParamItems =
                    resourceParamMetadata.getXpathExecutable().load(new DynamicXpathOptionsImpl().setContextItem(contentDocElem)).evaluateItems()).isEmpty()) {
                continue;
            }

            for (XdmItem resourceParamItem : resourceParamItems) {
                switch (resourceParamMetadata.getType()) {
                    case COMPOSITE:
                        this.buildCompositeResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case DATE:
                        this.buildDateResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case NUMBER:
                        this.buildNumberResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case QUANTITY:
                        this.buildQuantityResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case REFERENCE:
                        this.buildReferenceResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case STRING:
                        this.buildStringResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case TOKEN:
                        this.buildTokenResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;

                    case URI:
                        this.buildUriResourceParam(type, resourceParams, resourceParamName, resourceParamMetadata, resourceParamItem, entity);
                        break;
                }
            }
        }

        Set<MultiKey<Serializable>> existingResourceParamKeys = new LinkedHashSet<>(existingResourceParams.keySet()),
            resourceParamKeys = new LinkedHashSet<>(resourceParams.keySet()),
            removeResourceParamKeys = new LinkedHashSet<>(SetUtils.difference(existingResourceParamKeys, resourceParamKeys)),
            addResourceParamKeys = new LinkedHashSet<>(SetUtils.difference(resourceParamKeys, existingResourceParamKeys));
        Set<Integer> removeResourceParamKeyHashCodes = removeResourceParamKeys.stream()
            .map(removeResourceParamKey -> System.identityHashCode(existingResourceParams.get(removeResourceParamKey))).collect(Collectors.toSet());
        Iterator<ResourceParam<?>> existingResourceParamIterator = iterateExistingResourceParams(entity);

        while (existingResourceParamIterator.hasNext()) {
            if (removeResourceParamKeyHashCodes.contains(System.identityHashCode(existingResourceParamIterator.next()))) {
                existingResourceParamIterator.remove();
            }
        }

        ResourceParam<?> addResourceParam;

        for (MultiKey<Serializable> addResourceParamKey : addResourceParamKeys) {
            switch ((addResourceParam = resourceParams.get(addResourceParamKey)).getType()) {
                case DATE:
                    entity.addDateParams(((DateResourceParam) addResourceParam));
                    break;

                case NUMBER:
                    entity.addNumberParams(((NumberResourceParam) addResourceParam));
                    break;

                case QUANTITY:
                    entity.addQuantityParams(((QuantityResourceParam) addResourceParam));
                    break;

                case REFERENCE:
                    entity.addRefParams(((RefResourceParam) addResourceParam));
                    break;

                case STRING:
                    entity.addStringParams(((StringResourceParam) addResourceParam));
                    break;

                case TOKEN:
                    entity.addTokenParams(((TokenResourceParam) addResourceParam));
                    break;

                case URI:
                    entity.addUriParams(((UriResourceParam) addResourceParam));
                    break;
            }
        }

        LOGGER
            .trace(String.format(
                "Processed resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) parameter(s): removedKeys=[%s], addedKeys=[%s], resultKeys=[%s]",
                type, entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(),
                removeResourceParamKeys.stream()
                    .map(removeResourceParamKey -> (SdcctStringUtils.L_BRACE + StringUtils.join(removeResourceParamKey.getKeys(), ", ") +
                        SdcctStringUtils.R_BRACE))
                    .collect(Collectors.joining("; ")),
                addResourceParamKeys.stream()
                    .map(addResourceParamKey -> (SdcctStringUtils.L_BRACE + StringUtils.join(addResourceParamKey.getKeys(), ", ") + SdcctStringUtils.R_BRACE))
                    .collect(Collectors.joining("; ")),
                mapExistingResourceParams(entity).keySet().stream().map(
                    resultResourceParamKey -> (SdcctStringUtils.L_BRACE + StringUtils.join(resultResourceParamKey.getKeys(), ", ") + SdcctStringUtils.R_BRACE))
                    .collect(Collectors.joining("; "))));
    }

    protected void buildCompositeResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        throw new HibernateException(String.format(
            "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) composite parameter (name=%s, item=%s).", type,
            entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
    }

    protected void buildDateResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        throw new HibernateException(String.format(
            "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) date parameter (name=%s, item=%s).", type,
            entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
    }

    protected void buildNumberResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        if (item instanceof XdmAtomicValue) {
            BigDecimal value = ((XdmAtomicValue) item).getDecimalValue();

            resourceParams.put(new MultiKey<>(name, value), new NumberResourceParamImpl(entity, name, value));
        } else {
            throw new HibernateException(String.format(
                "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) number parameter (name=%s, item=%s).",
                type, entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
        }
    }

    protected void buildQuantityResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        throw new HibernateException(String.format(
            "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) quantity parameter (name=%s, item=%s).", type,
            entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
    }

    protected void buildReferenceResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        throw new HibernateException(String.format(
            "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) reference parameter (name=%s, item=%s).", type,
            entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
    }

    protected void buildStringResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        if (item instanceof XdmAtomicValue) {
            String value = item.getStringValue();

            resourceParams.put(new MultiKey<>(name, value), new StringResourceParamImpl(entity, name, value));
        } else {
            throw new HibernateException(String.format(
                "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) string parameter (name=%s, item=%s).",
                type, entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
        }
    }

    protected void buildTokenResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name,
        ResourceParamMetadata metadata, XdmItem item, V entity) throws Exception {
        if (item instanceof XdmAtomicValue) {
            String value = item.getStringValue();

            resourceParams.put(new MultiKey<>(name, value), new TokenResourceParamImpl(entity, false, name, null, null, null, value));
        } else {
            throw new HibernateException(String.format(
                "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) token parameter (name=%s, item=%s).", type,
                entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
        }
    }

    protected void buildUriResourceParam(String type, Map<MultiKey<Serializable>, ResourceParam<?>> resourceParams, String name, ResourceParamMetadata metadata,
        XdmItem item, V entity) throws Exception {
        throw new HibernateException(String.format(
            "Unable to build resource (type=%s) entity (class=%s, entityId=%s, id=%s, instanceId=%s, version=%s) URI parameter (name=%s, item=%s).", type,
            entity.getClass(), entity.getEntityId(), entity.getId(), entity.getInstanceId(), entity.getVersion(), name, item));
    }

    protected Object decodeNode(XdmNode node) throws Exception {
        Class<?> resultClass;

        try {
            resultClass = this.contentPathBuilder.build(true, node.getUnderlyingNode()).getSegments().getLast().getBeanClass();
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to build document (uri=%s) node (qname=%s, lineNum=%d, columnNum=%d) result class.",
                node.getDocumentURI(), node.getNodeName().getClarkName(), node.getLineNumber(), node.getColumnNumber()), e);
        }

        try {
            return this.xmlCodec.decode(node.getUnderlyingNode(), resultClass, null);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to decode (resultClass=%s) document (uri=%s) node (qname=%s, lineNum=%d, columnNum=%d).",
                resultClass.getName(), node.getDocumentURI(), node.getNodeName().getClarkName(), node.getLineNumber(), node.getColumnNumber()), e);
        }
    }

    @Override
    public ContentPathBuilder getContentPathBuilder() {
        return this.contentPathBuilder;
    }

    @Override
    public void setContentPathBuilder(ContentPathBuilder contentPathBuilder) {
        this.contentPathBuilder = contentPathBuilder;
    }
}
