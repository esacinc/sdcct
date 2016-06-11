package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.NumberResourceParam;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(ResourceParamTypeNames.NUMBER)
@Entity(name = "resourceParamNum")
@Table(name = DbTableNames.RESOURCE_PARAM_NUMBER)
public class NumberResourceParamImpl extends AbstractResourceParam<BigDecimal> implements NumberResourceParam {
    private final static long serialVersionUID = 0L;

    public NumberResourceParamImpl(@Nullable SdcctResource resource, String name, BigDecimal value) {
        super(resource, ResourceParamType.NUMBER, false, name, value);
    }

    public NumberResourceParamImpl() {
        super(ResourceParamType.NUMBER);
    }

    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public BigDecimal getValue() {
        return super.getValue();
    }
}
