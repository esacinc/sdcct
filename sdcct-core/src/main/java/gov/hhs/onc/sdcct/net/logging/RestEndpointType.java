package gov.hhs.onc.sdcct.net.logging;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum RestEndpointType implements IdentifiedBean {
    SERVER, CLIENT;

    private final String id;

    private RestEndpointType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
