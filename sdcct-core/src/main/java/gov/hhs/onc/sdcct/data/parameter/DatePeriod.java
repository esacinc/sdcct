package gov.hhs.onc.sdcct.data.parameter;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.Nullable;

public interface DatePeriod extends Serializable {
    public boolean hasEndValue();

    @Nullable
    public Date getEndValue();

    public void setEndValue(@Nullable Date endValue);

    public boolean hasStartValue();

    @Nullable
    public Date getStartValue();

    public void setStartValue(@Nullable Date startValue);
}
