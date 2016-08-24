package gov.hhs.onc.sdcct.fhir.ws.metadata;

import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.ws.metadata.ResourceWsMetadata;

public interface FhirResourceWsMetadata<T extends Resource> extends ResourceWsMetadata<T, FhirResourceMetadata<T>, FhirInteractionWsMetadata> {
}
