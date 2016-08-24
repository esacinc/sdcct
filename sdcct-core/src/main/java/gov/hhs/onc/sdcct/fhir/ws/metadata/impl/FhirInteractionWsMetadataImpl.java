package gov.hhs.onc.sdcct.fhir.ws.metadata.impl;

import gov.hhs.onc.sdcct.fhir.ws.metadata.FhirInteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.WsInteractionType;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractInteractionWsMetadata;

public class FhirInteractionWsMetadataImpl extends AbstractInteractionWsMetadata implements FhirInteractionWsMetadata {
    public FhirInteractionWsMetadataImpl(WsInteractionType type) {
        super(type);
    }
}
