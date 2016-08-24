package gov.hhs.onc.sdcct.rfd.ws.metadata.impl;

import gov.hhs.onc.sdcct.rfd.ws.metadata.RfdInteractionWsMetadata;
import gov.hhs.onc.sdcct.ws.WsInteractionType;
import gov.hhs.onc.sdcct.ws.metadata.impl.AbstractInteractionWsMetadata;

public class RfdInteractionWsMetadataImpl extends AbstractInteractionWsMetadata implements RfdInteractionWsMetadata {
    public RfdInteractionWsMetadataImpl(WsInteractionType type) {
        super(type);
    }
}
