package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.rfd.impl.RfdResourceImpl;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = RfdResourceImpl.class, idClass = Long.class)
public interface RfdResourceRepository extends SdcctRepository<RfdResource> {
}
