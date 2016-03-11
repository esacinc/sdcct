package gov.hhs.onc.sdcct.data;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.data.db.DbTableNames;

public enum ResourceType implements IdentifiedBean {
    FORM_FHIR(DbTableNames.FORM_FHIR), FORM_RESP_FHIR(DbTableNames.FORM_RESP_FHIR), FORM_RFD(DbTableNames.FORM_RFD), FORM_RESP_RFD(DbTableNames.FORM_RESP_RFD);

    private final String id;

    private ResourceType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
