package gov.hhs.onc.sdcct.data.db.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.sdcct.data.db.DbParamModeType;
import gov.hhs.onc.sdcct.data.db.DbParamNullabilityType;
import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

public interface DbParam {
    @JsonProperty
    @Nonnegative
    public int getIndex();

    @JsonProperty
    public DbParamModeType getMode();

    @JsonProperty
    public DbParamNullabilityType getNullability();

    @JsonProperty
    @Nonnegative
    public int getPrecision();

    @JsonProperty
    @Nonnegative
    public int getScale();

    @JsonProperty
    public int getType();

    @JsonProperty
    public String getTypeName();

    public boolean hasValue();

    @JsonProperty
    @Nullable
    public Object getValue();

    public boolean hasValueString();

    @Nullable
    public String getValueString();
}
