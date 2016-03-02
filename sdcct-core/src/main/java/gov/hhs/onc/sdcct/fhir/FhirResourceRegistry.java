package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.db.SdcctDao;
import gov.hhs.onc.sdcct.data.db.SdcctDataService;
import gov.hhs.onc.sdcct.data.db.SdcctRegistry;

public interface FhirResourceRegistry<T extends DomainResource, U extends FhirResource, V extends SdcctDao<U>, W extends SdcctDataService<U, V>> extends
    SdcctRegistry<T, U, V, W> {
}
