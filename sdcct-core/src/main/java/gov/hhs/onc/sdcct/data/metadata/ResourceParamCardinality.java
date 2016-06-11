package gov.hhs.onc.sdcct.data.metadata;

import javax.annotation.Nonnegative;

public interface ResourceParamCardinality {
    public boolean isRequired();

    public boolean isUnbounded();

    @Nonnegative
    public int getMaximum();

    public void setMaximum(@Nonnegative int max);

    @Nonnegative
    public int getMinimum();

    public void setMinimum(@Nonnegative int min);
}
