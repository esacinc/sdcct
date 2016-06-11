package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.QuantityResourceParam;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import java.math.BigDecimal;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(ResourceParamTypeNames.QUANTITY)
@Entity(name = "resourceParamQuantity")
@Table(name = DbTableNames.RESOURCE_PARAM_QUANTITY)
public class QuantityResourceParamImpl extends AbstractTermResourceParam<BigDecimal> implements QuantityResourceParam {
    private final static long serialVersionUID = 0L;

    private String units;

    public QuantityResourceParamImpl(@Nullable SdcctResource resource, String name, @Nullable URI codeSystemUri, @Nullable String codeSystemVersion,
        @Nullable String units, @Nullable String displayName, BigDecimal value) {
        super(resource, ResourceParamType.QUANTITY, false, name, codeSystemUri, codeSystemVersion, displayName, value);

        this.units = units;
    }

    public QuantityResourceParamImpl() {
        super(ResourceParamType.QUANTITY);
    }

    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Override
    public boolean hasUnits() {
        return (this.units != null);
    }

    @Column(name = DbColumnNames.UNITS)
    @Nullable
    @Override
    public String getUnits() {
        return this.units;
    }

    @Override
    public void setUnits(@Nullable String units) {
        this.units = units;
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public BigDecimal getValue() {
        return super.getValue();
    }
}
