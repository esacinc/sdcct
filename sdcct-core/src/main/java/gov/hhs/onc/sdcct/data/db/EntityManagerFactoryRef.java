package gov.hhs.onc.sdcct.data.db;

import org.hibernate.jpa.internal.EntityManagerFactoryImpl;

public interface EntityManagerFactoryRef extends DbService {
    public EntityManagerFactoryImpl getEntityManagerFactory();

    public void setEntityManagerFactory(EntityManagerFactoryImpl entityManagerFactory);
}
