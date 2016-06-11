package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.DateResourceParam;
import gov.hhs.onc.sdcct.data.parameter.DatePeriod;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import javax.annotation.Nullable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Target;

@DiscriminatorValue(ResourceParamTypeNames.DATE)
@Entity(name = "resourceParamDate")
@Table(name = DbTableNames.RESOURCE_PARAM_DATE)
public class DateResourceParamImpl extends AbstractResourceParam<DatePeriod> implements DateResourceParam {
    private final static long serialVersionUID = 0L;

    public DateResourceParamImpl(@Nullable SdcctResource resource, String name, DatePeriod value) {
        super(resource, ResourceParamType.DATE, false, name, value);
    }

    public DateResourceParamImpl() {
        super(ResourceParamType.DATE);
    }

    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Embedded
    @Override
    @Target(DatePeriodImpl.class)
    public DatePeriod getValue() {
        return super.getValue();
    }
}
