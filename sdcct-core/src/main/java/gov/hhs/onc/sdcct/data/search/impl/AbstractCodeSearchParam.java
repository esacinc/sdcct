package gov.hhs.onc.sdcct.data.search.impl;

import gov.hhs.onc.sdcct.data.ResourceEntity;
import gov.hhs.onc.sdcct.data.db.DbColumnNames;
import gov.hhs.onc.sdcct.data.search.CodeSearchParam;
import gov.hhs.onc.sdcct.data.search.SearchParamType;
import java.net.URI;
import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCodeSearchParam<T> extends AbstractSearchParam<T> implements CodeSearchParam<T> {
    protected URI codeSystemUri;

    protected AbstractCodeSearchParam(SearchParamType type, @Nullable ResourceEntity resource, String name, @Nullable URI codeSystemUri, T value) {
        super(type, resource, name, value);

        this.codeSystemUri = codeSystemUri;
    }

    protected AbstractCodeSearchParam(SearchParamType type) {
        super(type);
    }

    public boolean hasCodeSystemUri() {
        return (this.codeSystemUri != null);
    }

    @Column(name = DbColumnNames.CODE_SYSTEM_URI)
    @Nullable
    @Override
    public URI getCodeSystemUri() {
        return this.codeSystemUri;
    }

    @Override
    public void setCodeSystemUri(@Nullable URI codeSystemUri) {
        this.codeSystemUri = codeSystemUri;
    }
}
