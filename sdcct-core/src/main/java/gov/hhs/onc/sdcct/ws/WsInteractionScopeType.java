package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum WsInteractionScopeType implements IdentifiedBean {
    SYSTEM, TYPE, INSTANCE, HISTORY_INSTANCE;

    private final String id;

    private WsInteractionScopeType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
