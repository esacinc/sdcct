package gov.hhs.onc.sdcct.testcases;

import gov.hhs.onc.sdcct.api.RoleNames;
import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum SpecificationRole implements IdentifiedBean {
    FORM_ARCHIVER(RoleNames.FORM_ARCHIVER), FORM_FILLER(RoleNames.FORM_FILLER), FORM_MANAGER(RoleNames.FORM_MANAGER), FORM_RECEIVER(RoleNames.FORM_RECEIVER);

    private final String id;

    private SpecificationRole(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
