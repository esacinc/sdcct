package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.parameter.DatePeriod;
import java.util.Date;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DatePeriodImpl implements DatePeriod {
    private final static long serialVersionUID = 0L;

    private Date startValue;
    private Date endValue;

    public DatePeriodImpl(@Nullable Date startValue, @Nullable Date endValue) {
        this();

        this.startValue = startValue;
        this.endValue = endValue;
    }

    public DatePeriodImpl() {
    }

    @Override
    public boolean hasEndValue() {
        return (this.endValue != null);
    }

    @Column(name = DbColumnNames.VALUE_END)
    @Nullable
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEndValue() {
        return this.endValue;
    }

    @Override
    public void setEndValue(@Nullable Date endValue) {
        this.endValue = endValue;
    }

    @Override
    public boolean hasStartValue() {
        return (this.startValue != null);
    }

    @Column(name = DbColumnNames.VALUE_START)
    @Nullable
    @Override
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartValue() {
        return this.startValue;
    }

    @Override
    public void setStartValue(@Nullable Date startValue) {
        this.startValue = startValue;
    }
}
