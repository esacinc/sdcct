package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.ResourceProcessor;

public interface FhirResourceProcessor extends ResourceProcessor<ResourceType, DomainResource, FhirResource, FhirResourceMetadata<? extends DomainResource>> {
}
