package gov.hhs.onc.sdcct.data.db.metadata;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbService;
import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import java.util.Map;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.impl.SearchFactoryReference;
import org.hibernate.service.spi.Startable;

public interface DbMetadataService extends DbService, Startable {
    public Map<Class<? extends SdcctEntity>, EntityMetadata> getEntityMetadatas();

    public EntityManagerFactoryImpl getEntityManagerFactory();

    public void setEntityManagerFactory(EntityManagerFactoryRef entityManagerFactoryRef);

    public MetadataImpl getMetadata();

    public void setMetadata(MetadataRef metadataRef);

    public ExtendedSearchIntegrator getSearchIntegrator();

    public void setSearchIntegrator(SearchFactoryReference searchFactoryRef);
}
