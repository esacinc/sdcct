package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.db.DbTableNames;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import gov.hhs.onc.sdcct.data.search.UriSearchParam;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "searchParamUri")
@Table(name = DbTableNames.SEARCH_PARAM_URI)
public class UriSearchParamImpl extends AbstractSearchParam implements UriSearchParam {
    private URI value;

    public UriSearchParamImpl(String name, @Nullable Long resourceEntityId, URI value) {
        super(SearchParamType.URI, resourceEntityId, name);

        this.value = value;
    }

    public UriSearchParamImpl() {
        super(SearchParamType.URI);
    }

    @Column(name = DbColumnNames.VALUE, nullable = false)
    @Override
    public URI getValue() {
        return this.value;
    }

    @Override
    public void setValue(URI value) {
        this.value = value;
    }
}
