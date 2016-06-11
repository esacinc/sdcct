package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import gov.hhs.onc.sdcct.data.parameter.UriResourceParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(ResourceParamTypeNames.URI)
@Entity(name = "resourceParamUri")
@Table(name = DbTableNames.RESOURCE_PARAM_URI)
public class UriResourceParamImpl extends AbstractResourceParam<URI> implements UriResourceParam {
    private final static long serialVersionUID = 0L;

    public UriResourceParamImpl(@Nullable SdcctResource resource, Boolean meta, String name, URI value) {
        super(resource, ResourceParamType.URI, meta, name, value);
    }

    public UriResourceParamImpl() {
        super(ResourceParamType.URI);
    }

    @ManyToOne(optional = false, targetEntity = SdcctResourceImpl.class)
    @Override
    public SdcctResource getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public URI getValue() {
        return super.getValue();
    }
}
