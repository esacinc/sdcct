package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import gov.hhs.onc.sdcct.data.db.cache.impl.CacheRegionFactory;
import gov.hhs.onc.sdcct.data.db.impl.SdcctHsqlDialect.SdcctHsqlDialectResolver;
import gov.hhs.onc.sdcct.data.db.logging.impl.LoggingEntityIndexingInterceptor;
import gov.hhs.onc.sdcct.data.db.metadata.DbMetadataService;
import gov.hhs.onc.sdcct.data.db.metadata.MetadataRef;
import java.util.Map;
import javax.annotation.Nullable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.boot.spi.PersistenceUnitDescriptor;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.search.engine.impl.DefaultMutableEntityIndexBinding;
import org.hibernate.search.engine.impl.MutableEntityIndexBinding;
import org.hibernate.search.hcore.util.impl.ContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

public class SdcctEntityManagerFactoryFactoryBean extends LocalContainerEntityManagerFactoryBean {
    @SuppressWarnings({ CompilerWarnings.RAWTYPES, CompilerWarnings.UNCHECKED })
    private class SdcctEntityManagerFactoryBuilder extends EntityManagerFactoryBuilderImpl {
        public SdcctEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnit, Map props, @Nullable ClassLoader classLoader) {
            super(persistenceUnit, props, classLoader);
        }

        @Override
        public EntityManagerFactoryImpl build() {
            SessionFactoryImpl sessionFactory =
                ((SessionFactoryImpl) (SdcctEntityManagerFactoryFactoryBean.this.entityManagerFactory = ((EntityManagerFactoryImpl) super.build()))
                    .getSessionFactory());
            StandardServiceRegistry serviceRegistry = sessionFactory.getSessionFactoryOptions().getServiceRegistry();

            serviceRegistry.getService(EntityManagerFactoryRef.class).setEntityManagerFactory(SdcctEntityManagerFactoryFactoryBean.this.entityManagerFactory);

            ContextHelper.getSearchintegratorBySFI(sessionFactory).getIndexBindings().replaceAll((entityClass, entityIndexBinding) -> {
                MutableEntityIndexBinding newEntityIndexBinding =
                    new DefaultMutableEntityIndexBinding(entityIndexBinding.getSelectionStrategy(), entityIndexBinding.getSimilarity(),
                        entityIndexBinding.getIndexManagers(), SdcctEntityManagerFactoryFactoryBean.this.entityIndexingInterceptor);
                newEntityIndexBinding.setDocumentBuilderIndexedEntity(entityIndexBinding.getDocumentBuilder());

                return newEntityIndexBinding;
            });

            serviceRegistry.getService(DbMetadataService.class);

            return SdcctEntityManagerFactoryFactoryBean.this.entityManagerFactory;
        }

        @Override
        protected void populate(SessionFactoryBuilder sessionFactoryBuilder, StandardServiceRegistry serviceRegistry) {
            serviceRegistry.getService(MetadataRef.class).setMetadata(this.getMetadata());

            serviceRegistry.getService(CacheRegionFactory.class);
            serviceRegistry.getService(SdcctHsqlDialectResolver.class);

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
        protected EntityManagerFactoryBuilder getEntityManagerFactoryBuilder(PersistenceUnitDescriptor persistenceUnitDescriptor, Map props,
            @Nullable ClassLoader classLoader) {
            return new SdcctEntityManagerFactoryBuilder(persistenceUnitDescriptor, props, classLoader);
        }
    }

    private final static long serialVersionUID = 0L;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiredMembersInspection" })
    private LoggingEntityIndexingInterceptor entityIndexingInterceptor;

    private EntityManagerFactoryImpl entityManagerFactory;

    @Override
    protected EntityManagerFactory createNativeEntityManagerFactory() throws PersistenceException {
        this.setPersistenceProvider(new SdcctPersistenceProvider());

        return super.createNativeEntityManagerFactory();
    }
}
