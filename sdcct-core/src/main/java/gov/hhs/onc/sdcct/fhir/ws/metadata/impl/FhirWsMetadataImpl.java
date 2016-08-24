package gov.hhs.onc.sdcct.fhir.ws.metadata.impl;

import gov.hhs.onc.sdcct.api.SpecificationType;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirInteractionWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirResourceWsMetadata;
import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirWsMetadata;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractWsMetadata;

public class FhirWsMetadataImpl extends AbstractWsMetadata<FhirInteractionWsMetadata, FhirResourceWsMetadata<?>> implements FhirWsMetadata {
    public FhirWsMetadataImpl(String id, String name) {
        super(SpecificationType.FHIR, id, name);
    }
}
