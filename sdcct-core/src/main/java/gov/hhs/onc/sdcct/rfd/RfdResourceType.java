package gov.hhs.onc.sdcct.rfd;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.NamedBean;

public enum RfdResourceType implements IdentifiedBean, NamedBean {
    FORM_DESIGN("FormDesignType", "FormDesign");

    private final String id;
    private final String name;

    private RfdResourceType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
