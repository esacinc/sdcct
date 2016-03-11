package gov.hhs.onc.sdcct.data.search;

import java.math.BigDecimal;
import javax.annotation.Nullable;

public interface QuantitySearchParam extends CodeSearchParam<BigDecimal> {
    public boolean hasUnits();

    @Nullable
    public String getUnits();

    public void setUnits(@Nullable String units);
}
