package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.metadata.ResourceMetadata;

public interface FhirResourceMetadata<T extends DomainResource> extends ResourceMetadata<ResourceType, T> {
}
