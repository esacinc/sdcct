package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import gov.hhs.onc.sdcct.data.parameter.StringResourceParam;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(ResourceParamTypeNames.STRING)
@Entity(name = "resourceParamStr")
@Table(name = DbTableNames.RESOURCE_PARAM_STRING)
public class StringResourceParamImpl extends AbstractResourceParam<String> implements StringResourceParam {
    private final static long serialVersionUID = 0L;

    public StringResourceParamImpl(@Nullable SdcctResource resource, String name, String value) {
        super(resource, ResourceParamType.STRING, false, name, value);
    }

    public StringResourceParamImpl() {
        super(ResourceParamType.STRING);
    }

    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public String getValue() {
        return super.getValue();
    }
}
