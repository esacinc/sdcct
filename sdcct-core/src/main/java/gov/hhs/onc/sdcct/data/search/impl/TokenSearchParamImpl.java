package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.SdcctResource;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.SdcctResourceImpl;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.SearchParamTypeNames;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(SearchParamTypeNames.TOKEN)
@Entity(name = "searchParamToken")
@Table(name = DbTableNames.SEARCH_PARAM_TOKEN)
public class TokenSearchParamImpl extends AbstractTermSearchParam<String> implements TokenSearchParam {
    private final static long serialVersionUID = 0L;

    public TokenSearchParamImpl(@Nullable SdcctResource resource, String name, @Nullable URI codeSystemUri, @Nullable String codeSystemVersion,
        @Nullable String displayName, String value) {
        super(SearchParamType.TOKEN, resource, name, codeSystemUri, codeSystemVersion, displayName, value);
    }

    public TokenSearchParamImpl() {
        super(SearchParamType.TOKEN);
    }

    @JoinColumns({ @JoinColumn(name = DbColumnNames.RESOURCE_ID, referencedColumnName = DbColumnNames.ID, updatable = false),
        @JoinColumn(name = DbColumnNames.RESOURCE_VERSION, referencedColumnName = DbColumnNames.VERSION, updatable = false) })
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
