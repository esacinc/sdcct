package gov.hhs.onc.sdcct.ws.metadata;

import gov.hhs.onc.sdcct.ws.WsInteractionType;

public interface InteractionWsMetadata extends WsMetadataComponent {
    public boolean isRestricted();

    public void setRestricted(boolean restricted);

    public WsInteractionType getType();
}
