package gov.hhs.onc.sdcct.data.parameter.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamType;
import gov.hhs.onc.sdcct.data.parameter.ResourceParamTypeNames;
import gov.hhs.onc.sdcct.data.parameter.TokenResourceParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(ResourceParamTypeNames.TOKEN)
@Entity(name = "resourceParamToken")
@Table(name = DbTableNames.RESOURCE_PARAM_TOKEN)
public class TokenResourceParamImpl extends AbstractTermResourceParam<String> implements TokenResourceParam {
    private final static long serialVersionUID = 0L;

    public TokenResourceParamImpl(@Nullable SdcctResource resource, Boolean meta, String name, @Nullable URI codeSystemUri, @Nullable String codeSystemVersion,
        @Nullable String displayName, String value) {
        super(resource, ResourceParamType.TOKEN, meta, name, codeSystemUri, codeSystemVersion, displayName, value);
    }

    public TokenResourceParamImpl() {
        super(ResourceParamType.TOKEN);
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
