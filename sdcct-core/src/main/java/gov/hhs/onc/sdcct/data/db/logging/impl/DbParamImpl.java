package gov.hhs.onc.sdcct.data.db.logging.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.data.db.DbParamModeType;
import gov.hhs.onc.sdcct.data.db.DbParamNullabilityType;
import gov.hhs.onc.sdcct.data.db.logging.DbParam;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public class DbParamImpl implements DbParam {
    private int index;
    private int type;
    private String typeName;
    private DbParamModeType mode;
    private DbParamNullabilityType nullability;
    private int precision;
    private int scale;
    private Object value;
    private String valueStr;

    public DbParamImpl(int index, int type, String typeName, DbParamModeType mode, DbParamNullabilityType nullability, @Nonnegative int precision,
        @Nonnegative int scale, @Nullable Object value, @Nullable String valueStr) {
        this.index = index;
        this.type = type;
        this.typeName = typeName;
        this.mode = mode;
        this.nullability = nullability;
        this.precision = precision;
        this.scale = scale;
        this.value = value;
        this.valueStr = valueStr;
    }

    @JsonProperty
    @Override
    public int getIndex() {
        return this.index;
    }

    @JsonProperty
    @Override
    public DbParamModeType getMode() {
        return this.mode;
    }

    @JsonProperty
    @Override
    public DbParamNullabilityType getNullability() {
        return this.nullability;
    }

    @JsonProperty
    @Nonnegative
    @Override
    public int getPrecision() {
        return this.precision;
    }

    @JsonProperty
    @Nonnegative
    @Override
    public int getScale() {
        return this.scale;
    }

    @JsonProperty
    @Override
    public int getType() {
        return this.type;
    }

    @JsonProperty
    @Override
    public String getTypeName() {
        return this.typeName;
    }

    @Override
    public boolean hasValue() {
        return (this.value != null);
    }

    @JsonProperty
    @Nullable
    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public boolean hasValueString() {
        return (this.valueStr != null);
    }

    @Nullable
    @Override
    public String getValueString() {
        return this.valueStr;
    }
}
