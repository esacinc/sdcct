package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.OrdinalIdentifiedBean;
import java.sql.ParameterMetaData;
import javax.annotation.Nonnegative;

public enum DbParamNullabilityType implements IdentifiedBean, OrdinalIdentifiedBean {
    NO_NULLS(ParameterMetaData.parameterNoNulls), NULLABLE(ParameterMetaData.parameterNullable), UNKNOWN(ParameterMetaData.parameterNullableUnknown);

    private final String id;
    private final int ordinalId;

    private DbParamNullabilityType(@Nonnegative int ordinalId) {
        this.id = this.name().toLowerCase();
        this.ordinalId = ordinalId;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getOrdinalId() {
        return this.ordinalId;
    }
}
