package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.db.SdcctResourceRegistry;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;

public interface FhirResourceRegistry<T extends Resource> extends SdcctResourceRegistry<T, FhirResourceMetadata<T>, FhirResource> {
}
