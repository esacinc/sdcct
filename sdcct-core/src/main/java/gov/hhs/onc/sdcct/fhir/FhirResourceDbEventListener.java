package gov.hhs.onc.sdcct.fhir;

import gov.hhs.onc.sdcct.data.db.event.ResourceDbEventListener;

public interface FhirResourceDbEventListener extends
    ResourceDbEventListener<ResourceType, DomainResource, FhirResource, FhirResourceMetadata<? extends DomainResource>, FhirResourceProcessor> {
}
