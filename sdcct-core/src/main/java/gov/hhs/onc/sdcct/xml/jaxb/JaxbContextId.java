package gov.hhs.onc.sdcct.xml.jaxb;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum JaxbContextId implements IdentifiedBean {
    RFD, FHIR;

    private final String id;

    private JaxbContextId() {
        this.id = this.name();
    }

    @Override
    public String getId() {
        return this.id;
    }
}
