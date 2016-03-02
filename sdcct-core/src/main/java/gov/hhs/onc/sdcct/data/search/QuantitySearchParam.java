package gov.hhs.onc.sdcct.data.search;

import java.math.BigDecimal;
import javax.annotation.Nullable;

public interface QuantitySearchParam extends CodeSearchParam {
    public boolean hasUnits();

    @Nullable
    public String getUnits();

    public void setUnits(@Nullable String units);

    public BigDecimal getValue();

    public void setValue(BigDecimal value);
}
