package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.rfd.impl.RfdResourceImpl;
import java.math.BigInteger;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = RfdResourceImpl.class, idClass = BigInteger.class)
public interface RfdResourceRepository extends SdcctRepository<RfdResource> {
}
