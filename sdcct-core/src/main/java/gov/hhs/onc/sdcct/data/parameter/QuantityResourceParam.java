package gov.hhs.onc.sdcct.data.parameter;

import java.math.BigDecimal;
import javax.annotation.Nullable;

public interface QuantityResourceParam extends TermResourceParam<BigDecimal> {
    public boolean hasUnits();

    @Nullable
    public String getUnits();

    public void setUnits(@Nullable String units);
}
