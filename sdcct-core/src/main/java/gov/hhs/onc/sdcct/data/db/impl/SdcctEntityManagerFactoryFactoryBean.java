package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.cache.impl.CacheRegionFactory;
import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import gov.hhs.onc.sdcct.data.db.event.DbEventListener;
import gov.hhs.onc.sdcct.data.metadata.MetadataRef;
import gov.hhs.onc.sdcct.data.metadata.MetadataService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitInfo;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.IntegratorProvider;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.search.engine.impl.DefaultMutableEntityIndexBinding;
import org.hibernate.search.engine.impl.MutableEntityIndexBinding;
import org.hibernate.search.hcore.util.impl.ContextHelper;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class SdcctEntityManagerFactoryFactoryBean extends LocalContainerEntityManagerFactoryBean {
    private class SdcctIntegrator implements Integrator {
        @Override
        public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        }

        @Override
        public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
            serviceRegistry.locateServiceBinding(RegionFactory.class).setService(SdcctEntityManagerFactoryFactoryBean.this.cacheRegionFactory);

            Arrays.sort(SdcctEntityManagerFactoryFactoryBean.this.eventListeners, AnnotationAwareOrderComparator.INSTANCE);

            EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
            eventListenerRegistry.prependListeners(EventType.SAVE, SdcctEntityManagerFactoryFactoryBean.this.eventListeners);
            eventListenerRegistry.prependListeners(EventType.SAVE_UPDATE, SdcctEntityManagerFactoryFactoryBean.this.eventListeners);
        }
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    private class SdcctEntityManagerFactoryBuilder extends EntityManagerFactoryBuilderImpl {
        public SdcctEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnit, Map props, @Nullable ClassLoader classLoader) {
            super(persistenceUnit, props, classLoader);
        }

        @Override
        public EntityManagerFactoryImpl build() {
            EntityManagerFactoryImpl entityManagerFactory = ((EntityManagerFactoryImpl) super.build());
            SessionFactoryImpl sessionFactory = ((SessionFactoryImpl) entityManagerFactory.getSessionFactory());
            StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();

            serviceRegistry.getService(EntityManagerFactoryRef.class).setEntityManagerFactory(entityManagerFactory);

            ContextHelper
                .getSearchintegratorBySFI(sessionFactory)
                .getIndexBindings()
                .replaceAll(
                    (entityClass, entityIndexBinding) -> {
                        MutableEntityIndexBinding newEntityIndexBinding =
                            new DefaultMutableEntityIndexBinding(entityIndexBinding.getSelectionStrategy(), entityIndexBinding.getSimilarity(),
                                entityIndexBinding.getIndexManagers(), SdcctEntityManagerFactoryFactoryBean.this.entityIndexingInterceptor);
                        newEntityIndexBinding.setDocumentBuilderIndexedEntity(entityIndexBinding.getDocumentBuilder());

                        return newEntityIndexBinding;
                    });

            serviceRegistry.getService(MetadataService.class);

            return entityManagerFactory;
        }

        @Override
        protected void populate(SessionFactoryBuilder sessionFactoryBuilder, StandardServiceRegistry serviceRegistry) {
            serviceRegistry.getService(MetadataRef.class).setMetadata(this.getMetadata());

            super.populate(sessionFactoryBuilder, serviceRegistry);
        }

        @Override
        public MetadataImpl getMetadata() {
            return ((MetadataImpl) super.getMetadata());
        }
    }

    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    private class SdcctPersistenceProvider extends HibernatePersistenceProvider {
        @Override
        public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo persistenceUnitInfo, Map props) {
            props.put(EntityManagerFactoryBuilderImpl.INTEGRATOR_PROVIDER, ((IntegratorProvider) () -> Collections.singletonList(new SdcctIntegrator())));

            return super.createContainerEntityManagerFactory(persistenceUnitInfo, props);
        }

        @Override
        protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnitDescriptor, Map props,
            @Nullable ClassLoader classLoader) {
            return new SdcctEntityManagerFactoryBuilder(persistenceUnitDescriptor, props, classLoader);
        }
    }

    private final static long serialVersionUID = 0L;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private DbEventListener<?>[] eventListeners;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private LoggingEntityIndexingInterceptor entityIndexingInterceptor;

    private CacheRegionFactory cacheRegionFactory;

    @Override
    protected EntityManagerFactory createNativeEntityManagerFactory() throws PersistenceException {
        this.setPersistenceProvider(new SdcctPersistenceProvider());

        return super.createNativeEntityManagerFactory();
    }

    public CacheRegionFactory getCacheRegionFactory() {
        return this.cacheRegionFactory;
    }

    public void setCacheRegionFactory(CacheRegionFactory cacheRegionFactory) {
        this.cacheRegionFactory = cacheRegionFactory;
    }
}
