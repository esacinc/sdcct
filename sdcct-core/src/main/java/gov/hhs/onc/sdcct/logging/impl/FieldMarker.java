package gov.hhs.onc.sdcct.logging.impl;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FieldMarker extends AbstractSdcctMarker {
    private final static long serialVersionUID = 0L;

    private String fieldName;
    private Object fieldValue;

    public FieldMarker(String fieldName, Object fieldValue) {
        super("FIELD");

        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof FieldMarker)) {
            return false;
        }

        FieldMarker fieldMarker = ((FieldMarker) obj);

        return (this.fieldName.equals(fieldMarker.fieldName) && Objects.equals(this.fieldValue, fieldMarker.fieldValue));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(this.fieldName).append(this.fieldValue).toHashCode();
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public Object getFieldValue() {
        return this.fieldValue;
    }
}
