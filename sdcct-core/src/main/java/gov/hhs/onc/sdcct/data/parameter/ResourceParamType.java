package gov.hhs.onc.sdcct.data.parameter;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;

public enum ResourceParamType implements IdentifiedBean {
    COMPOSITE(ResourceParamTypeNames.COMPOSITE), DATE(ResourceParamTypeNames.DATE), NUMBER(ResourceParamTypeNames.NUMBER),
    QUANTITY(ResourceParamTypeNames.QUANTITY), REFERENCE(ResourceParamTypeNames.REFERENCE), STRING(ResourceParamTypeNames.STRING),
    TOKEN(ResourceParamTypeNames.TOKEN), URI(ResourceParamTypeNames.URI);

    private final String id;

    private ResourceParamType(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
