package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import gov.hhs.onc.sdcct.beans.IdentifiedBean

enum CodegenTypeKind implements IdentifiedBean {
    DATATYPE, PRIMITIVE, RESOURCE;

    private final String id;

    private CodegenTypeKind() {
        this.id = this.name().toLowerCase();
    }

    @Override
    public String getId() {
        return this.id;
    }
}