package gov.hhs.onc.sdcct.data.search;

import java.math.BigDecimal;

public interface NumberSearchParam extends SearchParam {
    public BigDecimal getValue();

    public void setValue(BigDecimal value);
}
