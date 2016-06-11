package gov.hhs.onc.sdcct.data.db.impl;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.SdcctEntityAccessor;
import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.data.db.metadata.DbMetadataService;
import gov.hhs.onc.sdcct.data.db.metadata.EntityMetadata;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Metamodel;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.AbstractRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.data.util.ClassTypeInformation;

public class SdcctRepositoryFactoryBean<T extends SdcctEntity> extends TransactionalRepositoryFactoryBeanSupport<SdcctRepository<T>, T, Long>
    implements SdcctEntityAccessor<T> {
    private class SdcctRepositoryMetadata extends AbstractRepositoryMetadata {
        public SdcctRepositoryMetadata() {
            super(SdcctRepositoryFactoryBean.this.repoInterface);
        }

        @Override
        public Set<Class<?>> getAlternativeDomainTypes() {
            return SdcctRepositoryFactoryBean.this.entityClasses;
        }

        @Override
        public Class<?> getDomainType() {
            return SdcctRepositoryFactoryBean.this.entityImplClass;
        }

        @Override
        public Class<? extends Serializable> getIdType() {
            return Long.class;
        }
    }

    private class SdcctRepositoryFactory extends RepositoryFactorySupport {
        @Override
        protected Object getTargetRepository(RepositoryInformation repoInfo) {
            return this.getTargetRepositoryViaReflection(repoInfo, entityManager, SdcctRepositoryFactoryBean.this.entityClass,
                SdcctRepositoryFactoryBean.this.entityImplClass, SdcctRepositoryFactoryBean.this.entityMetadata);
        }

        @Override
        public <T, U extends Serializable> EntityInformation<T, U> getEntityInformation(Class<T> domainClass) {
            return new JpaMetamodelEntityInformation<>(domainClass, SdcctRepositoryFactoryBean.this.metamodel);
        }

        @Override
        @SuppressWarnings({ CompilerWarnings.UNCHECKED })
        protected RepositoryMetadata getRepositoryMetadata(Class<?> repoInterface) {
            return new SdcctRepositoryMetadata();
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return SdcctRepositoryFactoryBean.this.repoBaseClass;
        }
    }

    private Class<T> entityClass;
    private Class<? extends T> entityImplClass;
    private Set<Class<?>> entityClasses;
    private EntityManager entityManager;
    private EntityMetadata entityMetadata;
    private Metamodel metamodel;
    private Class<? extends SdcctRepository<T>> repoBaseClass;
    private Class<? extends SdcctRepository<T>> repoInterface;

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void afterPropertiesSet() {
        this.entityMetadata = this.entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getSessionFactoryOptions().getServiceRegistry()
            .getService(DbMetadataService.class).getEntityMetadatas().get((this.entityClass =
                ((Class<T>) ClassTypeInformation.from(this.repoInterface).getSuperTypeInformation(SdcctRepository.class).getTypeArguments().get(0).getType())));
        this.entityClasses =
            Stream.of(this.entityClass, (this.entityImplClass = ((Class<? extends T>) this.entityMetadata.getMappedClass()))).collect(Collectors.toSet());
        this.metamodel = this.entityManager.getMetamodel();

        super.afterPropertiesSet();
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        return new SdcctRepositoryFactory();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.setEnableDefaultTransactions(false);

        super.setBeanFactory(beanFactory);
    }

    @Override
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    @Override
    public Class<? extends T> getEntityImplClass() {
        return this.entityImplClass;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
    }

    @Override
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public void setRepositoryBaseClass(Class<?> repoBaseClass) {
        super.setRepositoryBaseClass((this.repoBaseClass = ((Class<? extends SdcctRepository<T>>) repoBaseClass)));
    }

    @Override
    public void setRepositoryInterface(Class<? extends SdcctRepository<T>> repoInterface) {
        super.setRepositoryInterface((this.repoInterface = repoInterface));
    }
}
