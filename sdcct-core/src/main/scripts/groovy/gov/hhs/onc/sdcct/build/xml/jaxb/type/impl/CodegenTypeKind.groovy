package gov.hhs.onc.sdcct.build.xml.jaxb.type.impl

import gov.hhs.onc.sdcct.beans.IdentifiedBean
import javax.annotation.Nullable

enum CodegenTypeKind implements IdentifiedBean {
    COMPLEX("complex-type"), LOGICAL(null), PRIMITIVE("primitive-type"), RESOURCE(null);

    private final String id;

    private CodegenTypeKind(@Nullable String id) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
    }

    @Override
    public String getId() {
        return this.id;
    }
}