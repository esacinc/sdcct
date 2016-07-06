package gov.hhs.onc.sdcct.api;

import gov.hhs.onc.sdcct.api.SpecificationNames;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum SpecificationType implements IdentifiedBean {
    FHIR(SpecificationNames.FHIR), RFD(SpecificationNames.RFD);

    private final String id;

    private SpecificationType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
