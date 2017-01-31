package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.TypeBean;
import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbPropertyNames;
import gov.hhs.onc.sdcct.data.db.DbQueryNames;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriteria;
import gov.hhs.onc.sdcct.data.db.criteria.SdcctCriterion;
import gov.hhs.onc.sdcct.data.db.criteria.utils.SdcctCriterionUtils;
import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;
import gov.hhs.onc.sdcct.data.metadata.ResourceParamMetadata;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamProcessor;
import gov.hhs.onc.sdcct.transform.impl.ByteArraySource;
import gov.hhs.onc.sdcct.xml.XmlEncodeOptions;
import gov.hhs.onc.sdcct.xml.saxon.impl.SdcctDocumentBuilder;
import gov.hhs.onc.sdcct.xml.saxon.impl.XdmDocument;
import gov.hhs.onc.sdcct.xml.impl.XmlCodec;
import gov.hhs.onc.sdcct.xml.xpath.impl.DynamicXpathOptionsImpl;
import gov.hhs.onc.sdcct.xml.xpath.saxon.impl.SdcctXpathExecutable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.transform.Source;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.MutableNodeInfo;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.tree.linked.DocumentImpl;
import net.sf.saxon.value.Whitespace;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSdcctResourceRegistry<T, U extends ResourceMetadata<T>, V extends SdcctResource> extends AbstractSdcctResourceAccessor<T, V>
    implements SdcctResourceRegistry<T, U, V> {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected ResourceParamProcessor<T, ? super U, V> resourceParamProc;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    protected SdcctRepository<V> repo;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    protected XmlCodec xmlCodec;

    @Autowired
    protected SdcctDocumentBuilder docBuilder;

    protected U resourceMetadata;
    protected Supplier<V> entityInstantiator;
    protected CriteriaBuilder criteriaBuilder;
    protected Map<String, ResourceParamMetadata> resourceParamMetadatas;

    static {
        Init.init();
    }

    protected AbstractSdcctResourceRegistry(U resourceMetadata, Class<V> entityClass, Class<? extends V> entityImplClass, Supplier<V> entityInstantiator) {
        super(resourceMetadata.getSpecificationType(), resourceMetadata.getBeanClass(), resourceMetadata.getBeanImplClass(), entityClass, entityImplClass);

        this.resourceMetadata = resourceMetadata;
        this.entityInstantiator = entityInstantiator;
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional
    public boolean removeByEntityId(@Nonnegative long entityId) throws Exception {
        return (this.remove(this.buildCriteria(SdcctCriterionUtils.matchEntityId(entityId))) > 0);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional
    public long remove(SdcctCriteria<V> criteria) throws Exception {
        Date timestamp = new Date();
        List<V> entities = criteria.list(this.entityManager);

        for (V entity : entities) {
            entity.setDeletedTimestamp(timestamp);

            this.entityManager.merge(entity);
        }

        return entities.size();
    }

    @Override
    @Transactional
    public V saveBean(T bean) throws Exception {
        return this.saveBean(bean, this.entityInstantiator.get());
    }

    @Override
    @Transactional
    public V saveBean(T bean, V entity) throws Exception {
        if (!entity.hasEntityId()) {
            entity.setType(((TypeBean) bean).getTypePath());
        }

        entity.setContent(this.buildContent(this.buildContentDocument(new ByteArraySource(this.xmlCodec.encode(bean, null))), false));

        return this.save(entity);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public V save(V entity) throws Exception {
        ByteArraySource contentSrc = new ByteArraySource(entity.getContent().getBytes(StandardCharsets.UTF_8));
        XdmDocument contentDoc = this.buildContentDocument(contentSrc);
        String content = this.buildContent(contentDoc, true);

        if (entity.hasEntityId()) {
            V persistentEntity = this.entityManager.find(this.entityImplClass, entity.getEntityId());
            this.entityManager.detach(persistentEntity);

            if (!content.equals(persistentEntity.getContent())) {
                entity = this.entityInstantiator.get();
                entity.setId(persistentEntity.getId());
                entity.setInstanceId(persistentEntity.getInstanceId());
                entity.setPublishedTimestamp(persistentEntity.getPublishedTimestamp());
                entity.setType(persistentEntity.getType());
            }
        }

        Long id = entity.getId(), instanceId = entity.getInstanceId(), version = entity.getVersion();
        CriteriaQuery<Long> criteriaQuery;
        Root<V> criteriaRoot;

        if (id == null) {
            id = this.entityManager.createNamedQuery(DbQueryNames.RESOURCE_SELECT_ID_NEW, Long.class).getSingleResult();
            instanceId = -1L;
            version = 1L;
        } else if (instanceId == null) {
            (criteriaQuery = this.criteriaBuilder.createQuery(Long.class))
                .select(this.criteriaBuilder.max((criteriaRoot = ((Root<V>) (criteriaQuery.from(this.entityImplClass)))).get(DbPropertyNames.INSTANCE_ID)));
            criteriaQuery.distinct(true);

            instanceId = (Optional
                .ofNullable(this.buildCriteria(SdcctCriterionUtils.matchId(id), SdcctCriterionUtils.matchInstances(), SdcctCriterionUtils.matchLatestVersion())
                    .first(this.entityManager, criteriaQuery, criteriaRoot))
                .orElse(0L) + 1);
            version = 1L;
        } else if (version == null) {
            (criteriaQuery = this.criteriaBuilder.createQuery(Long.class))
                .select(this.criteriaBuilder.max((criteriaRoot = ((Root<V>) (criteriaQuery.from(this.entityImplClass)))).get(DbPropertyNames.VERSION)));
            criteriaQuery.distinct(true);

            version = (Optional.ofNullable(this.buildCriteria(SdcctCriterionUtils.matchId(id), SdcctCriterionUtils.matchInstanceId(instanceId))
                .first(this.entityManager, criteriaQuery, criteriaRoot)).orElse(0L) + 1);
        }

        entity.setId(id);
        entity.setInstanceId(instanceId);
        entity.setVersion(version);

        Date timestamp = new Date();
        entity.setModifiedTimestamp(timestamp);

        if (!entity.hasPublishedTimestamp()) {
            entity.setPublishedTimestamp(timestamp);
        }

        this.resourceParamProc
            .process((contentDoc = this.buildContentDocument(new ByteArraySource(this.xmlCodec.encode(this.buildBean(contentSrc, entity), null)))), entity);

        entity.setContent(this.buildContent(contentDoc, true));

        return this.repo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAllBeans(SdcctCriteria<V> criteria) throws Exception {
        List<V> entities = this.findAll(criteria);
        List<T> beans = new ArrayList<>(entities.size());

        for (V entity : entities) {
            beans.add(this.buildBean(entity));
        }

        return beans;
    }

    @Override
    @Transactional(readOnly = true)
    public List<V> findAll(SdcctCriteria<V> criteria) throws Exception {
        return this.repo.findAll(criteria);
    }

    @Nullable
    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    @Transactional(readOnly = true)
    public T findBeanByEntityId(@Nonnegative long entityId) throws Exception {
        return this.findBean(this.buildCriteria(SdcctCriterionUtils.matchEntityId(entityId)));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public V findByEntityId(@Nonnegative long entityId) throws Exception {
        return this.repo.findByEntityId(entityId);
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public T findBean(SdcctCriteria<V> criteria) throws Exception {
        V entity = this.find(criteria);

        return ((entity != null) ? this.buildBean(entity) : null);
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public V find(SdcctCriteria<V> criteria) throws Exception {
        return this.repo.find(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEntityId(@Nonnegative long entityId) throws Exception {
        return this.repo.existsByEntityId(entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(SdcctCriteria<V> criteria) throws Exception {
        return this.repo.exists(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public long count(SdcctCriteria<V> criteria) throws Exception {
        return this.repo.count(criteria);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public SdcctCriteria<V> buildCriteria(SdcctCriterion<V> ... criterions) {
        return this.repo.buildCriteria(criterions);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.resourceParamMetadatas = this.resourceMetadata.getParamMetadatas();
    }

    protected String buildContent(XdmDocument contentDoc, boolean canonical) throws Exception {
        DocumentImpl contentDocInfo = contentDoc.getUnderlyingNode();
        Source contentSrc;
        XmlEncodeOptions contentEncodeOpts = this.xmlCodec.getDefaultEncodeOptions().clone();

        if (canonical) {
            for (SdcctXpathExecutable canonicalRemoveXpathExec : this.resourceMetadata.getCanonicalRemoveXpathExecutables()) {
                for (XdmNode canonicalRemoveNode : canonicalRemoveXpathExec.load(new DynamicXpathOptionsImpl().setContextItem(contentDocInfo))
                    .evaluateNodes()) {
                    ((MutableNodeInfo) canonicalRemoveNode.getUnderlyingNode()).delete();
                }
            }

            contentEncodeOpts.getParseOptions().setStripSpace(Whitespace.ALL);

            contentSrc = new ByteArraySource(Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N11_OMIT_COMMENTS)
                .canonicalizeSubtree(NodeOverNodeInfo.wrap(contentDoc.getUnderlyingNode())), contentDoc.getPublicId(), contentDoc.getSystemId());
        } else {
            contentSrc = contentDoc.getUnderlyingNode();
        }

        return new String(this.xmlCodec.encode(contentSrc, contentEncodeOpts), StandardCharsets.UTF_8);
    }

    protected XdmDocument buildContentDocument(Source contentSrc) throws Exception {
        return this.docBuilder.build(contentSrc);
    }

    protected T buildBean(V entity) throws Exception {
        return this.buildBean(new ByteArraySource(entity.getContent().getBytes(StandardCharsets.UTF_8)), entity);
    }

    protected T buildBean(Source contentSrc, V entity) throws Exception {
        return this.buildBean(this.xmlCodec.decode(contentSrc, this.beanImplClass, null), entity);
    }

    protected T buildBean(T bean, V entity) throws Exception {
        return bean;
    }
}
