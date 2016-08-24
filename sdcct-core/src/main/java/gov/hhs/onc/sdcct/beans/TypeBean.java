package gov.hhs.onc.sdcct.beans;

import gov.hhs.onc.sdcct.api.type.DatatypeKindType;

public interface TypeBean {
    public DatatypeKindType getTypeKind();

    public String getTypePath();
}
