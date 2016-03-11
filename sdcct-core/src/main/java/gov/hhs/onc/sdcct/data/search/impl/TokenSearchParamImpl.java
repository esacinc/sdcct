package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.impl.AbstractResourceEntity;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.TokenSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@DiscriminatorValue(DbTableNames.SEARCH_PARAM_TOKEN)
@Entity(name = "searchParamToken")
@Table(name = DbTableNames.SEARCH_PARAM_TOKEN)
public class TokenSearchParamImpl extends AbstractCodeSearchParam<String> implements TokenSearchParam {
    public TokenSearchParamImpl(@Nullable ResourceEntity resource, String name, @Nullable URI codeSystemUri, String value) {
        super(SearchParamType.TOKEN, resource, name, codeSystemUri, value);
    }

    public TokenSearchParamImpl() {
        super(SearchParamType.TOKEN);
    }

    @JoinColumn(name = DbColumnNames.RESOURCE_ENTITY_ID, updatable = false)
    @ManyToOne(optional = false, targetEntity = AbstractResourceEntity.class)
    @Override
    public ResourceEntity getResource() {
        return super.getResource();
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public String getValue() {
        return super.getValue();
    }
}
