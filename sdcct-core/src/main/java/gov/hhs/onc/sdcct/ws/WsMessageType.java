package gov.hhs.onc.sdcct.ws;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum WsMessageType implements IdentifiedBean {
    REST, SOAP;

    private final String id;

    private WsMessageType() {
        this.id = this.name();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
