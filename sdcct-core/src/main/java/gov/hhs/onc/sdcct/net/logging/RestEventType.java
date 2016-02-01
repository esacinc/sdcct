package gov.hhs.onc.sdcct.net.logging;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum RestEventType implements IdentifiedBean {
    REQUEST, RESPONSE;

    private final String id;

    private RestEventType() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
