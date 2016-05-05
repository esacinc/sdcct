package gov.hhs.onc.sdcct.data.metadata;

import gov.hhs.onc.sdcct.data.SdcctEntity;
import gov.hhs.onc.sdcct.data.db.DbService;
import gov.hhs.onc.sdcct.data.db.EntityManagerFactoryRef;
import gov.hhs.onc.sdcct.fhir.DomainResource;
import gov.hhs.onc.sdcct.fhir.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.ResourceType;
import gov.hhs.onc.sdcct.rfd.RfdResourceMetadata;
import gov.hhs.onc.sdcct.rfd.RfdResourceType;
import gov.hhs.onc.sdcct.sdc.IdentifiedExtensionType;
import java.util.Map;
import org.hibernate.boot.internal.MetadataImpl;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.impl.SearchFactoryReference;
import org.hibernate.service.spi.Startable;

public interface MetadataService extends DbService, Startable {
    public Map<Class<? extends SdcctEntity>, EntityMetadata> getEntityMetadatas();

    public EntityManagerFactoryImpl getEntityManagerFactory();

    public void setEntityManagerFactory(EntityManagerFactoryRef entityManagerFactoryRef);

    public Map<ResourceType, FhirResourceMetadata<? extends DomainResource>> getFhirResourceMetadatas();

    public MetadataImpl getMetadata();

    public void setMetadata(MetadataRef metadataRef);

    public Map<RfdResourceType, RfdResourceMetadata<? extends IdentifiedExtensionType>> getRfdResourceMetadatas();

    public ExtendedSearchIntegrator getSearchIntegrator();

    public void setSearchIntegrator(SearchFactoryReference searchFactoryRef);
}
