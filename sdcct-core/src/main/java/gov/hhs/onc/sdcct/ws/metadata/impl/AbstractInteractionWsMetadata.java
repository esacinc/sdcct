package gov.hhs.onc.sdcct.ws.metadata.impl;

import gov.hhs.onc.sdcct.ws.WsInteractionType;
import gov.hhs.onc.sdcct.ws.metadata.InteractionWsMetadata;

public abstract class AbstractInteractionWsMetadata extends AbstractWsMetadataComponent implements InteractionWsMetadata {
    protected WsInteractionType type;
    protected boolean restricted;

    protected AbstractInteractionWsMetadata(WsInteractionType type) {
        super(type.getId(), type.getId());

        this.type = type;
    }

    @Override
    public boolean isRestricted() {
        return this.restricted;
    }

    @Override
    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    @Override
    public WsInteractionType getType() {
        return this.type;
    }
}
