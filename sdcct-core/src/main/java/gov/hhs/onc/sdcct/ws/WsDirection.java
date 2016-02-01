package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum WsDirection implements IdentifiedBean {
    INBOUND, OUTBOUND;

    private final String id;

    private WsDirection() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
