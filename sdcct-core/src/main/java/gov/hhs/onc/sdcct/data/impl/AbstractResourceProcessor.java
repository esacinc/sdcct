package gov.hhs.onc.sdcct.data.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.ResourceProcessor;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.SearchParamMetadata;
import gov.hhs.onc.sdcct.data.search.DateSearchParam;
import gov.hhs.onc.sdcct.data.search.NumberSearchParam;
import gov.hhs.onc.sdcct.data.search.QuantitySearchParam;
import gov.hhs.onc.sdcct.data.search.RefSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.StringSearchParam;
import gov.hhs.onc.sdcct.data.search.TermSearchParam;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import gov.hhs.onc.sdcct.data.search.impl.NumberSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.StringSearchParamImpl;
import gov.hhs.onc.sdcct.data.search.impl.TokenSearchParamImpl;
import gov.hhs.onc.sdcct.io.impl.ByteArraySource;
import gov.hhs.onc.sdcct.xml.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptions;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;
import javax.xml.transform.Source;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractResourceProcessor<T extends Enum<T> & IdentifiedBean, U, V extends SdcctResource, W extends ResourceMetadata<T, ? extends U>>
    implements ResourceProcessor<T, U, V, W> {
    @Autowired
    protected MetadataService metadataService;

    @Autowired
    protected XmlCodec xmlCodec;

    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    protected Class<U> beanSuperClass;
    protected Class<V> entitySuperClass;
    protected Function<MetadataService, Map<T, W>> resourceMetadatasGetter;

    protected AbstractResourceProcessor(Class<U> beanSuperClass, Class<V> entitySuperClass, Function<MetadataService, Map<T, W>> resourceMetadatasGetter) {
        this.beanSuperClass = beanSuperClass;
        this.entitySuperClass = entitySuperClass;
        this.resourceMetadatasGetter = resourceMetadatasGetter;
    }

    @Override
    public void process(V resource, T type) throws HibernateException {
        try {
            this.processSearchParams(resource, type);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to process resource (type=%s) search parameters.", type.getId()), e);
        }
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected static Map<MultiKey<Serializable>, SearchParam<?>> mapExistingSearchParams(SdcctResource resource) {
        Map<MultiKey<Serializable>, SearchParam<?>> existingSearchParams = new LinkedHashMap<>();
        Builder<Serializable> existingSearchParamKeyStreamBuilder;
        TermSearchParam<?> existingTermSearchParam;

        for (SearchParam<?> existingSearchParam : IteratorUtils.asIterable(iterateExistingSearchParams(resource))) {
            (existingSearchParamKeyStreamBuilder = Stream.builder()).add(existingSearchParam.getName());

            if (existingSearchParam instanceof TermSearchParam) {
                existingSearchParamKeyStreamBuilder.add((existingTermSearchParam = ((TermSearchParam<?>) existingSearchParam)).getCodeSystemUri());
                existingSearchParamKeyStreamBuilder.add(existingTermSearchParam.getCodeSystemVersion());

                if (existingSearchParam instanceof QuantitySearchParam) {
                    existingSearchParamKeyStreamBuilder.add(((QuantitySearchParam) existingTermSearchParam).getUnits());
                }

                existingSearchParamKeyStreamBuilder.add(existingTermSearchParam.getDisplayName());
            }

            existingSearchParamKeyStreamBuilder.add(existingSearchParam.getValue());

            existingSearchParams.put(new MultiKey<>(existingSearchParamKeyStreamBuilder.build().toArray(Serializable[]::new)), existingSearchParam);
        }

        return existingSearchParams;
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    protected static Iterator<SearchParam<?>> iterateExistingSearchParams(SdcctResource resource) {
        return IteratorUtils.chainedIterator(resource.getDateSearchParams().iterator(), resource.getNumberSearchParams().iterator(), resource
            .getQuantitySearchParams().iterator(), resource.getRefSearchParams().iterator(), resource.getStringSearchParams().iterator(), resource
            .getTokenSearchParams().iterator(), resource.getUriSearchParams().iterator());
    }

    protected void processSearchParams(V resource, T type) throws Exception {
        String content = resource.getContent();
        XdmDocument doc = this.buildDocument(type, new ByteArraySource(content.getBytes(StandardCharsets.UTF_8)));
        W resourceMetadata = this.resourceMetadatasGetter.apply(this.metadataService).get(type);
        Map<String, SearchParamMetadata> searchParamMetadatas = resourceMetadata.getSearchParamMetadatas();
        Map<MultiKey<Serializable>, SearchParam<?>> existingSearchParams = mapExistingSearchParams(resource), searchParams = new LinkedHashMap<>();
        SearchParamMetadata searchParamMetadata;
        List<XdmItem> searchParamItems;

        for (String searchParamName : searchParamMetadatas.keySet()) {
            // noinspection ConstantConditions
            if ((searchParamMetadata = searchParamMetadatas.get(searchParamName)).isInline() || !searchParamMetadata.hasXpathExecutable()
                || (searchParamItems = searchParamMetadata.getXpathExecutable().load(new DynamicXpathOptions().setContextNode(doc)).evaluateItems()).isEmpty()) {
                continue;
            }

            for (XdmItem searchParamItem : searchParamItems) {
                switch (searchParamMetadata.getType()) {
                    case COMPOSITE:
                        this.buildCompositeSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case DATE:
                        this.buildDateSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case NUMBER:
                        this.buildNumberSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case QUANTITY:
                        this.buildQuantitySearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case REFERENCE:
                        this.buildReferenceSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case STRING:
                        this.buildStringSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case TOKEN:
                        this.buildTokenSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;

                    case URI:
                        this.buildUriSearchParam(resource, type, searchParams, searchParamName, searchParamMetadata, searchParamItem);
                        break;
                }
            }
        }

        Set<MultiKey<Serializable>> existingSearchParamKeys = new LinkedHashSet<>(existingSearchParams.keySet()), searchParamKeys = searchParams.keySet();
        Set<Integer> removeSearchParamHashCodes =
            CollectionUtils.removeAll(existingSearchParamKeys, searchParamKeys).stream()
                .map(removeSearchParamKey -> System.identityHashCode(existingSearchParams.get(removeSearchParamKey))).collect(Collectors.toSet());
        Iterator<SearchParam<?>> existingSearchParamIterator = iterateExistingSearchParams(resource);

        while (existingSearchParamIterator.hasNext()) {
            if (removeSearchParamHashCodes.contains(System.identityHashCode(existingSearchParamIterator.next()))) {
                existingSearchParamIterator.remove();
            }
        }

        SearchParam<?> addSearchParam;

        for (MultiKey<Serializable> addSearchParamKey : CollectionUtils.removeAll(searchParamKeys, existingSearchParamKeys)) {
            switch ((addSearchParam = searchParams.get(addSearchParamKey)).getType()) {
                case DATE:
                    resource.addDateSearchParams(((DateSearchParam) addSearchParam));
                    break;

                case NUMBER:
                    resource.addNumberSearchParams(((NumberSearchParam) addSearchParam));
                    break;

                case QUANTITY:
                    resource.addQuantitySearchParams(((QuantitySearchParam) addSearchParam));
                    break;

                case REFERENCE:
                    resource.addRefSearchParams(((RefSearchParam) addSearchParam));
                    break;

                case STRING:
                    resource.addStringSearchParams(((StringSearchParam) addSearchParam));
                    break;

                case TOKEN:
                    resource.addTokenSearchParams(((TokenSearchParam) addSearchParam));
                    break;

                case URI:
                    resource.addUriSearchParams(((UriSearchParam) addSearchParam));
                    break;
            }
        }
    }

    protected void buildCompositeSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        throw new HibernateException(String.format("Unable to build resource (type=%s) composite search parameter (name=%s, item=%s).", type.getId(), name,
            item));
    }

    protected void buildDateSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        throw new HibernateException(String.format("Unable to build resource (type=%s) date search parameter (name=%s, item=%s).", type.getId(), name, item));
    }

    protected void buildNumberSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        if (item instanceof XdmAtomicValue) {
            BigDecimal value = ((XdmAtomicValue) item).getDecimalValue();

            searchParams.put(new MultiKey<>(name, value), new NumberSearchParamImpl(resource, name, value));
        } else {
            throw new HibernateException(String.format("Unable to build resource (type=%s) number search parameter (name=%s, item=%s).", type.getId(), name,
                item));
        }
    }

    protected void buildQuantitySearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        throw new HibernateException(
            String.format("Unable to build resource (type=%s) quantity search parameter (name=%s, item=%s).", type.getId(), name, item));
    }

    protected void buildReferenceSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        throw new HibernateException(String.format("Unable to build resource (type=%s) reference search parameter (name=%s, item=%s).", type.getId(), name,
            item));
    }

    protected void buildStringSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        if (item instanceof XdmAtomicValue) {
            String value = item.getStringValue();

            searchParams.put(new MultiKey<>(name, value), new StringSearchParamImpl(resource, name, value));
        } else {
            throw new HibernateException(String.format("Unable to build resource (type=%s) string search parameter (name=%s, item=%s).", type.getId(), name,
                item));
        }
    }

    protected void buildTokenSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name,
        SearchParamMetadata metadata, XdmItem item) throws Exception {
        if (item instanceof XdmAtomicValue) {
            String value = item.getStringValue();

            searchParams.put(new MultiKey<>(name, value), new TokenSearchParamImpl(resource, name, null, null, null, value));
        } else {
            throw new HibernateException(String.format("Unable to build resource (type=%s) string search parameter (name=%s, item=%s).", type.getId(), name,
                item));
        }
    }

    protected void buildUriSearchParam(V resource, T type, Map<MultiKey<Serializable>, SearchParam<?>> searchParams, String name, SearchParamMetadata metadata,
        XdmItem item) throws Exception {
        throw new HibernateException(String.format("Unable to build resource (type=%s) URI search parameter (name=%s, item=%s).", type.getId(), name, item));
    }

    protected <X> X decodeNode(XdmNode node, Class<? extends X> resultClass) throws HibernateException {
        try {
            return this.xmlCodec.decode(node.getUnderlyingNode(), resultClass, null);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to decode (resultClass=%s) node (qname=%s, lineNum=%d, columnNum=%d) from document (uri=%s).",
                resultClass.getName(), node.getNodeName().getClarkName(), node.getLineNumber(), node.getColumnNumber(), node.getDocumentURI()), e);
        }
    }

    protected XdmDocument buildDocument(T type, Source docSrc) throws Exception {
        if (!this.resourceMetadatasGetter.apply(this.metadataService).containsKey(type)) {
            throw new HibernateException(String.format("Unknown resource type (name=%s).", type.name()));
        }

        return this.docBuilder.build(docSrc);
    }
}
