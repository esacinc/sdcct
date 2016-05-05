package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.db.SdcctRegistry;

public interface FhirResourceRegistry<T extends DomainResource> extends SdcctRegistry<T, FhirResource> {
}
