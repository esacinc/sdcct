package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.db.SdcctRepository;
import gov.hhs.onc.sdcct.fhir.impl.FhirResourceImpl;
import java.math.BigInteger;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = FhirResourceImpl.class, idClass = BigInteger.class)
public interface FhirResourceRepository extends SdcctRepository<FhirResource> {
}
