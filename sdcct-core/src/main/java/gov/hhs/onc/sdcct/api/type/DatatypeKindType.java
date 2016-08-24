package gov.hhs.onc.sdcct.api.type;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import javax.annotation.Nullable;

public enum DatatypeKindType implements IdentifiedBean {
    COMPLEX("complex-type"), LOGICAL(null), PRIMITIVE("primitive-type"), RESOURCE(null);

    private final String id;

    private DatatypeKindType(@Nullable String id) {
        this.id = ((id != null) ? id : this.name().toLowerCase());
    }

    @Override
    public String getId() {
        return this.id;
    }
}
