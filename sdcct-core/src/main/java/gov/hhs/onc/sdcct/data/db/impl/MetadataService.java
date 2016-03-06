package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbAnalyzerNames;
import gov.hhs.onc.sdcct.data.search.SearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamDef;
import gov.hhs.onc.sdcct.data.search.impl.SearchParamMetadata;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.persistence.OneToMany;
import org.apache.commons.collections4.IteratorUtils;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.metadata.IndexedTypeDescriptor;
import org.hibernate.search.metadata.PropertyDescriptor;
import org.hibernate.service.Service;

public class MetadataService implements Service {
    private final static long serialVersionUID = 0L;

    private Map<Class<? extends SdcctEntity>, EntityMetadata> entities = new TreeMap<>(Comparator.comparing(Class::getName));

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void initialize(MetadataImplementor metadata, ExtendedSearchIntegrator searchIntegrator) {
        Class<?> entityBindingMappedClass;
        Class<? extends SdcctEntity> entityMappedClass;
        EntityMetadata entityMetadata;
        Map<String, PropertyMetadata> entityPropMetadatas;
        Map<String, SearchParamMetadata> entitySearchParamMetadatas;
        IndexedTypeDescriptor indexedEntityDesc;
        String entityPropName, entityPropSearchParamName, entityPropFieldName;
        Getter entityPropGetter;
        Method entityPropGetterMethod;
        Class<? extends SearchParam> entityPropJoinClass;
        boolean entityIndexed, entityPropIndexed;
        PropertyDescriptor indexedEntityPropDesc;
        Set<SearchParamDef> entityPropSearchParamDefAnnos;
        PropertyMetadata entityPropMetadata;

        for (PersistentClass entityBinding : metadata.getEntityBindings()) {
            if (!SdcctEntity.class.isAssignableFrom((entityBindingMappedClass = entityBinding.getMappedClass()))) {
                continue;
            }

            entityPropMetadatas =
                (entityMetadata =
                    new EntityMetadata(entityBinding.getEntityName(),
                        (entityIndexed =
                            (indexedEntityDesc =
                                searchIntegrator.getIndexedTypeDescriptor((entityMappedClass = ((Class<? extends SdcctEntity>) entityBindingMappedClass))))
                                .isIndexed()), entityMappedClass, entityBinding.getTable().getName())).getProperties();

            entitySearchParamMetadatas = entityMetadata.getSearchParams();

            this.entities.put(entityMappedClass, entityMetadata);

            for (Property entityProp : IteratorUtils.asIterable(((Iterator<Property>) entityBinding.getPropertyIterator()))) {
                entityPropName = entityProp.getName();
                entityPropJoinClass =
                    ((entityPropGetterMethod = (entityPropGetter = entityProp.getGetter(entityMappedClass)).getMethod()).isAnnotationPresent(OneToMany.class)
                        ? ((Class<? extends SearchParam>) entityPropGetterMethod.getDeclaredAnnotation(OneToMany.class).targetEntity()) : null);
                entityPropIndexed =
                    (entityIndexed && ((indexedEntityPropDesc = indexedEntityDesc.getProperty(entityPropName)) != null) && !indexedEntityPropDesc.isId() && entityPropGetterMethod
                        .isAnnotationPresent(Fields.class));

                if (!(entityPropSearchParamDefAnnos =
                    findEntitySearchParamDefAnnotations(new LinkedHashSet<>(), entityMappedClass, entityPropGetter.getMethodName())).isEmpty()) {
                    for (SearchParamDef entityPropSearchParamDefAnno : entityPropSearchParamDefAnnos) {
                        entitySearchParamMetadatas.put((entityPropSearchParamName = entityPropSearchParamDefAnno.name()), new SearchParamMetadata(
                            entityPropSearchParamName, entityPropIndexed, entityPropName, entityPropJoinClass, entityPropSearchParamDefAnno.type()));
                    }
                }

                if (entityProp.getColumnSpan() == 0) {
                    continue;
                }

                entityPropMetadatas.put(entityPropName, (entityPropMetadata =
                    new PropertyMetadata(entityPropName, entityPropIndexed, ((Column) entityProp.getColumnIterator().next()).getName(), entityProp.getType())));

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
        }
    }

    private static Set<SearchParamDef> findEntitySearchParamDefAnnotations(Set<SearchParamDef> entityPropSearchParamDefAnnos, Class<?> entityPropClass,
        String entityPropGetterMethodName) {
        Method entityPropGetterMethod = null;

        try {
            entityPropGetterMethod = entityPropClass.getDeclaredMethod(entityPropGetterMethodName);
        } catch (NoSuchMethodException ignored) {
        }

        if (entityPropGetterMethod != null) {
            Stream.of(entityPropGetterMethod.getDeclaredAnnotationsByType(SearchParamDef.class)).forEach(entityPropSearchParamDefAnnos::add);
        }

        if ((entityPropClass = entityPropClass.getSuperclass()) != null) {
            findEntitySearchParamDefAnnotations(entityPropSearchParamDefAnnos, entityPropClass, entityPropGetterMethodName);
        }

        return entityPropSearchParamDefAnnos;
    }

    public Map<Class<? extends SdcctEntity>, EntityMetadata> getEntities() {
        return this.entities;
    }
}
