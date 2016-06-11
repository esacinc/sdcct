package gov.hhs.onc.sdcct.data.metadata.impl;

import gov.hhs.onc.sdcct.data.metadata.ResourceParamCardinality;
import gov.hhs.onc.sdcct.utils.SdcctStringUtils;
import javax.annotation.Nonnegative;

public class ResourceParamCardinalityImpl implements ResourceParamCardinality {
    private int min;
    private int max;

    public ResourceParamCardinalityImpl(@Nonnegative int min, String maxStr) {
        this(min, (maxStr.equals(SdcctStringUtils.ASTERISK) ? Integer.MAX_VALUE : Integer.parseUnsignedInt(maxStr)));
    }

    public ResourceParamCardinalityImpl(@Nonnegative int min, @Nonnegative int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isRequired() {
        return (this.min > 0);
    }

    @Override
    public boolean isUnbounded() {
        return (this.max == Integer.MAX_VALUE);
    }

    @Override
    public int getMaximum() {
        return this.max;
    }

    @Override
    public void setMaximum(@Nonnegative int max) {
        this.max = max;
    }

    @Override
    public int getMinimum() {
        return this.min;
    }

    @Override
    public void setMinimum(@Nonnegative int min) {
        this.min = min;
    }
}
