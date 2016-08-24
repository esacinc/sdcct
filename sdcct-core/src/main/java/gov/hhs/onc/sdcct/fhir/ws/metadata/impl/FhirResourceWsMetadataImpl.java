package gov.hhs.onc.sdcct.fhir.ws.metadata.impl;

import gov.hhs.onc.sdcct.fhir.Resource;
import gov.hhs.onc.sdcct.fhir.metadata.FhirResourceMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirInteractionWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirResourceWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractResourceWsMetadata;

public class FhirResourceWsMetadataImpl<T extends Resource> extends AbstractResourceWsMetadata<T, FhirResourceMetadata<T>, FhirInteractionWsMetadata> implements
    FhirResourceWsMetadata<T> {
    public FhirResourceWsMetadataImpl(FhirResourceMetadata<T> resourceMetadata) {
        super(resourceMetadata);
    }
}
