package gov.hhs.onc.sdcct.data.metadata.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.beans.NamedBean;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import gov.hhs.onc.sdcct.data.db.impl.AbstractDbServiceContributor;
import gov.hhs.onc.sdcct.data.metadata.EntityMetadata;
import gov.hhs.onc.sdcct.data.metadata.MetadataRef;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import gov.hhs.onc.sdcct.data.metadata.PropertyMetadata;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadataBuilder;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadataBuilder;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.IteratorUtils;
import org.hibernate.HibernateException;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.impl.SearchFactoryReference;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;
import org.hibernate.service.spi.InjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MetadataServiceImpl implements MetadataService {
    public static class MetadataServiceContributor extends AbstractDbServiceContributor<MetadataService> {
        public MetadataServiceContributor() {
            super(MetadataService.class);
        }
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(MetadataServiceImpl.class);

    private final static long serialVersionUID = 0L;

    @Autowired
    private FhirResourceMetadataBuilder fhirResourceBuilder;

    @Autowired
    private RfdResourceMetadataBuilder rfdResourceBuilder;

    private EntityManagerFactoryImpl entityManagerFactory;
    private MetadataImpl metadata;
    private ExtendedSearchIntegrator searchIntegrator;
    private Map<Class<? extends SdcctEntity>, EntityMetadata> entityMetadatas = new TreeMap<>(Comparator.comparing(Class::getName));
    private Map<ResourceType, FhirResourceMetadata<? extends DomainResource>> fhirResourceMetadatas = new EnumMap<>(ResourceType.class);
    private Map<RfdResourceType, RfdResourceMetadata<? extends IdentifiedExtensionType>> rfdResourceMetadatas = new EnumMap<>(RfdResourceType.class);

    @Override
    public void start() {
        try {
            this.processEntities();
            this.processResources();
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

    private void processResources() throws Exception {
        this.fhirResourceMetadatas.putAll(this.fhirResourceBuilder.build());
        this.rfdResourceMetadatas.putAll(this.rfdResourceBuilder.build());
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    private void processEntities() throws Exception {
        SessionFactoryImpl sessionFactory = ((SessionFactoryImpl) this.entityManagerFactory.getSessionFactory());
        Class<?> entityBindingMappedClass;
        Class<? extends SdcctEntity> entityMappedClass;
        String entityName, entityPropName, entityPropFieldName;
        EntityMetadata entityMetadata;
        Map<String, PropertyMetadata> entityPropMetadatas;
        IndexedTypeDescriptor indexedEntityDesc;
        Method entityPropGetterMethod;
        boolean entityIndexed, entityPropIndexed;
        PropertyDescriptor indexedEntityPropDesc;
        PropertyMetadata entityPropMetadata;
        BidiMap<Integer, String> entityPropOrder;
        String[] entityPropOrderNames;

        for (PersistentClass entityBinding : metadata.getEntityBindings()) {
            if (((entityBindingMappedClass = entityBinding.getMappedClass()) == null) || !SdcctEntity.class.isAssignableFrom(entityBindingMappedClass)) {
                continue;
            }

            entityPropMetadatas =
                (entityMetadata =
                    new EntityMetadataImpl((entityName = entityBinding.getEntityName()),
                        (entityIndexed =
                            (indexedEntityDesc =
                                searchIntegrator.getIndexedTypeDescriptor((entityMappedClass = ((Class<? extends SdcctEntity>) entityBindingMappedClass))))
                                .isIndexed()), entityMappedClass, entityBinding.getTable().getName())).getProperties();

            this.entityMetadatas.put(((Class<? extends SdcctEntity>) entityMappedClass.getInterfaces()[0]), entityMetadata);

            for (Property entityProp : IteratorUtils.asIterable(IteratorUtils.chainedIterator(((Iterator<Property>) entityBinding.getRootClass()
                .getPropertyIterator()), ((Iterator<Property>) entityBinding.getPropertyIterator())))) {
                entityPropName = entityProp.getName();
                entityPropGetterMethod = entityProp.getGetter(entityMappedClass).getMethod();
                entityPropIndexed =
                    (entityIndexed && ((indexedEntityPropDesc = indexedEntityDesc.getProperty(entityPropName)) != null) && !indexedEntityPropDesc.isId() && entityPropGetterMethod
                        .isAnnotationPresent(Fields.class));

                if (entityProp.getColumnSpan() == 0) {
                    continue;
                }

                entityPropMetadatas.put(
                    entityPropName,
                    (entityPropMetadata =
                        new PropertyMetadataImpl(entityPropName, entityPropIndexed, ((Column) entityProp.getColumnIterator().next()).getName(), entityProp
                            .getType())));

                if (entityPropIndexed) {
                    for (Field entityPropFieldAnno : entityPropGetterMethod.getAnnotation(Fields.class).value()) {
                        if (entityPropFieldAnno.analyze() == Analyze.NO) {
                            continue;
                        }

                        entityPropFieldName = entityPropFieldAnno.name();

                        switch (entityPropFieldAnno.analyzer().definition()) {
                            case DbAnalyzerNames.EDGE_NGRAM:
                                entityPropMetadata.setEdgeNgramFieldName(entityPropFieldName);
                                break;

                            case DbAnalyzerNames.LOWERCASE:
                                entityPropMetadata.setLowercaseFieldName(entityPropFieldName);
                                break;

                            case DbAnalyzerNames.NGRAM:
                                entityPropMetadata.setNgramFieldName(entityPropFieldName);
                                break;

                            case DbAnalyzerNames.PHONETIC:
                                entityPropMetadata.setPhoneticFieldName(entityPropFieldName);
                                break;
                        }
                    }
                }
            }

            entityMetadata.setIdProperty(entityPropMetadatas.get(entityBinding.getIdentifierProperty().getName()));

            entityPropOrder = entityMetadata.getPropertyOrder();
            entityPropOrderNames = sessionFactory.getEntityPersister(entityName).getPropertyNames();

            for (int a = 0; a < entityPropOrderNames.length; a++) {
                entityPropOrder.put(a, entityPropOrderNames[a]);
            }
        }

        LOGGER.debug(String.format("Processed metadata for %d entities: [%s]", this.entityMetadatas.size(),
            this.entityMetadatas.values().stream().map(NamedBean::getName).collect(Collectors.joining(", "))));
    }

    @Override
    public Map<Class<? extends SdcctEntity>, EntityMetadata> getEntityMetadatas() {
        return this.entityMetadatas;
    }

    @Override
    public EntityManagerFactoryImpl getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    @InjectService
    @Override
    public void setEntityManagerFactory(EntityManagerFactoryRef entityManagerFactoryRef) {
        this.entityManagerFactory = entityManagerFactoryRef.getEntityManagerFactory();
    }

    @Override
    public Map<ResourceType, FhirResourceMetadata<? extends DomainResource>> getFhirResourceMetadatas() {
        return this.fhirResourceMetadatas;
    }

    @Override
    public MetadataImpl getMetadata() {
        return this.metadata;
    }

    @InjectService
    @Override
    public void setMetadata(MetadataRef metadataRef) {
        this.metadata = metadataRef.getMetadata();
    }

    @Override
    public Map<RfdResourceType, RfdResourceMetadata<? extends IdentifiedExtensionType>> getRfdResourceMetadatas() {
        return this.rfdResourceMetadatas;
    }

    @Override
    public ExtendedSearchIntegrator getSearchIntegrator() {
        return this.searchIntegrator;
    }

    @InjectService
    @Override
    public void setSearchIntegrator(SearchFactoryReference searchFactoryRef) {
        this.searchIntegrator = searchFactoryRef.getSearchIntegrator();
    }
}
