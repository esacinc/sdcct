package gov.hhs.onc.sdcct.data.db.impl;

import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.springframework.stereotype.Component;

@Component("entityManagerFactoryRef")
public class EntityManagerFactoryRefImpl extends AbstractDbService implements EntityManagerFactoryRef {
    public static class EntityManagerFactoryRefContributor extends AbstractDbServiceContributor<EntityManagerFactoryRef> {
        public EntityManagerFactoryRefContributor() {
            super(EntityManagerFactoryRef.class);
        }
    }

    private final static long serialVersionUID = 0L;

    private EntityManagerFactoryImpl entityManagerFactory;

    @Override
    public EntityManagerFactoryImpl getEntityManagerFactory() {
        return this.entityManagerFactory;
    }

    @Override
    public void setEntityManagerFactory(EntityManagerFactoryImpl entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
