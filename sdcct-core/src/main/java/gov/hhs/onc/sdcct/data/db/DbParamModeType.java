package gov.hhs.onc.sdcct.data.db;

import gov.hhs.onc.sdcct.beans.IdentifiedBean;
import gov.hhs.onc.sdcct.beans.OrdinalIdentifiedBean;
import java.sql.ParameterMetaData;
import javax.annotation.Nonnegative;

public enum DbParamModeType implements IdentifiedBean, OrdinalIdentifiedBean {
    UNKNOWN(ParameterMetaData.parameterModeUnknown), IN(ParameterMetaData.parameterModeIn), IN_OUT(ParameterMetaData.parameterModeInOut), OUT(
        ParameterMetaData.parameterModeOut);

    private final String id;
    private final int ordinalId;

    private DbParamModeType(@Nonnegative int ordinalId) {
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
